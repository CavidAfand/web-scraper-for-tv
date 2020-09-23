package org.forbrightfuture.webscraperfortv.services.websites;

import lombok.extern.slf4j.Slf4j;
import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BakuElectronicsService implements Runnable {

    private List<Tv> tvList = new ArrayList<>();

    public List<Tv> getTvList() {
        return tvList;
    }

    private List<Tv> getAllTvs() {
        int pageCount = 1;
        while (true) {
            try {
                Document document = Jsoup.connect("https://www.bakuelectronics.az/catalog/tv-audio-video/televizorlar/?page=" + pageCount).get();

                // check page is valid or not
                int activeIndexInWebPage =Integer.parseInt(document.selectFirst("div.container div.content__holder div.content div.catalog__main div.mse2_pagination ul li a.active").text());
                if (pageCount != activeIndexInWebPage) break;
                pageCount++;

                List<Element> tvBlockHtml = document.select("div.catalog__col");

                tvBlockHtml.stream()
                        .map(i -> getTv(i.selectFirst("div.product__card a").attr("href")))
                        .forEach(i -> {
                            if (i != null)
                                tvList.add(i);
                        });
            }
            catch (IOException ex) {
                log.error("Error happened when html page https://www.bakuelectronics.az/catalog/tv-audio-video/televizorlar/?page=" + pageCount + " was loaded");
                return null;
            }
        }
        return tvList;
    }

    private Tv getTv(String tvLink) {
        try {
            Document tvPageHtml = Jsoup.connect(tvLink).get();

            List<Element> listHtml= tvPageHtml.select("div.specs-table ul.specs-table__set li");

            HashMap<String, String> params = new HashMap<>();
            // get other params and add them to hashmap
            listHtml.stream()
                    .forEach(i-> {
                        String paramKey = i.selectFirst("span.specs-table__spec").text().trim();
                        String paramValue = i.selectFirst("span.specs-table__text").text().trim();
                        params.put(paramKey.substring(0, paramKey.length()-1), paramValue);
                    });

            // get Tv brand
            String tvBrand = params.get("Brend").toUpperCase().trim();

            // get tv name
            String tvName = tvPageHtml.selectFirst("body div.wrapper main div.product__holder div.product__main div.product__info h1").text();

            // get tv price
            String priceString = tvPageHtml.selectFirst("body div.wrapper main div.product__container div.product__holder div.product__main div.product__info" +
                    " div.product__buy  div.product__price div.product__price--cur ").text();
            float price = Float.parseFloat(priceString);

            // get discount
            float discount = 0f;
            Element discountHtml = tvPageHtml.selectFirst("body div.wrapper main div.product__container div.product__holder div.product__main div.product__gallery " +
                    "div.product__gallery-main div.product__price--discount span.product__price--discount-price");
            if (discountHtml != null) {
                discount = Float.parseFloat(discountHtml.text());
            }

            // get tv image
            String imageLink = "https://bakuelectronics.az" + tvPageHtml.selectFirst("body div.wrapper main div.product__container div.product__holder " +
                    "div.product__main div.product__gallery div.product__gallery-main " +
                    "div.gallery__main div.gallery__main-slide a img").attr("src");

            // check Smart Tv functionality
            boolean smartTv = false;
            if (params.get("Smart TV") != null) smartTv = !params.get("Smart TV").equalsIgnoreCase("yox");

            // get tv screen type
            String screenType = null;
            if (params.get("Texnologiya") != null) screenType = params.get("Texnologiya").trim().toUpperCase();

            // check tv wifi support
            boolean wifiSupport = false;
            if (params.get("Wi-Fi") != null) wifiSupport = !params.get("Wi-Fi").trim().equalsIgnoreCase("yox");

            // get tv resolution
            String resolution = null;
            if (params.get("Ekran icazəsi") != null) resolution = getResolutionFromString(params.get("Ekran icazəsi"));

            // get tv diagonal
            int diagonalByInch = 0;
            if (params.get("Ekranın ölçüsü") != null)
                diagonalByInch = Integer.parseInt(params.get("Ekranın ölçüsü").substring(0, params.get("Ekranın ölçüsü").indexOf('"')).trim());
            int setDiagonalByCm = Math.round((float)diagonalByInch * 2.54f);

            return Tv.builder()
                    .brand(tvBrand)
                    .tvLink(tvLink)
                    .name(tvName.toUpperCase())
                    .price(price)
                    .discountInCashPayment(discount)
                    .diagonalByInch(diagonalByInch)
                    .diagonalByCm(setDiagonalByCm)
                    .resolution(resolution)
                    .screenType(screenType)
                    .imageLink(imageLink)
                    .smartTv(smartTv)
                    .wifiSupport(wifiSupport)
                    .active(true)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error happened when link " + tvLink + " was parsed. Error: " + ex.toString());
            return null;
        }
    }

    private String getResolutionFromString(String resolutionString) {
        if (resolutionString.equalsIgnoreCase("full hd")) return "1920x1080";
        if (resolutionString.equalsIgnoreCase("hd")) return "1366x768";
        if (resolutionString.equalsIgnoreCase("4k uhd")) return "3840x2160";
        if (resolutionString.equalsIgnoreCase("8k uhd")) return "7680x4320";
        return null;
    }

    @Override
    public void run() {
        // take start time
        long startTime = System.currentTimeMillis();
        log.info("BakuElectronics service began to parse");
        getAllTvs();
        long endTime = System.currentTimeMillis();
        log.info("BakuElectronics service ended to parse, spent time: " + ((float)(endTime - startTime) / 1000 / 60) + " minutes");
    }
}
