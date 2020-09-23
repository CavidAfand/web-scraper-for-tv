package org.forbrightfuture.webscraperfortv.services.websites;

import lombok.extern.slf4j.Slf4j;
import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.jsoup.HttpStatusException;
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
public class KontaktHomeService implements Runnable {

    private Document document = null;
    private List<Tv> tvList = new ArrayList<>();


    public List<Tv> getTvList() {
        return tvList;
    }

    private List<Tv> getAllTvs() {
        int pageCount = 1;
        while (true) {
            try {
                document = Jsoup.connect("https://kontakt.az/tv/televizor/page/" + (pageCount++) + "/").get();
            }
            catch (HttpStatusException ex) {
                break;
            }
            catch (IOException ex) {
                log.error("Error happened when html page https://kontakt.az/tv/televizor/page/" + (pageCount-1) + " was loaded");
            }

            List<Element> htmlElements = document.select("div.cart-item");

            htmlElements.stream()
                    .map(i -> getTv(i.selectFirst("div.imgHolder a").attr("href")))
                    .forEach(i -> {
                        if (i != null)
                            tvList.add(i);
                    });
        }

        return tvList;

    }

    private Tv getTv(String tvLink) {

        try {
            Document tvPageHtml = Jsoup.connect(tvLink).get();

            // get tv name
            String tvName = tvPageHtml.selectFirst("div#product-info form.product-features div.wrapper div.feature-container h1.title").text();

            // get tv image link
            String imageLink = tvPageHtml.selectFirst("div#product-info div.product-img-view div.menu-view div.mainImg a").attr("href");

            // get html part for price and discount
            Element prices = tvPageHtml.selectFirst("div#product-info form.product-features div.cont-price div.price");

            // get tv price
            float price = Float.parseFloat(prices.selectFirst("p span.nprice").text().trim());

            // get discount for tv
            float discount = 0.0f;
            Element discountHtml = prices.selectFirst("div.lbl-price-discount div.discount p");
            if (discountHtml != null) {
                String discountRaw = discountHtml.text();
                discount = Float.parseFloat(discountRaw.substring(1, discountRaw.length()-1).trim());
            }

            // get other params for tv
            List<Element> paramKey = tvPageHtml.select("div.col-lg-4 ul.features-review-tabs li");
            List<Element> paramValue = tvPageHtml.select("div.col-lg-8 ul.features-review-tabs li");

            HashMap<String, String> params = new HashMap<>();
            for (int i=0; i < paramKey.size(); i++) {
                params.put(paramKey.get(i).text().trim(), paramValue.get(i).text().trim());
            }

            // get tv brand
            String tvBrand = params.get("İstehsalçı").trim().toUpperCase();

            // get tv screen type
            String screenType = params.get("Ekranın növü").trim().toUpperCase();

            // get tv wifi support
            boolean wifiSupport = (params.get("Wi-Fi")!=null)?!params.get("Wi-Fi").equalsIgnoreCase("yox"):false;

            // get smart tv functionality
            boolean smartTv = (params.get("Smart TV")!=null)?!params.get("Smart TV").equalsIgnoreCase("yox"):false;

            // get tv diagonal by inch and cm
            int diagonalByInch = Integer.parseInt(params.get("Ekranın diagonalı").substring(0, params.get("Ekranın diagonalı").indexOf('"')));
            int diagonalByCm = Math.round((float)diagonalByInch * 2.54f);

            // get tv resolution
            String resolutionString =  params.get("Ekran icazəsi");
            String resolution = resolutionString.substring(resolutionString.indexOf('(')+1, resolutionString.indexOf(')'));

            return Tv.builder()
                    .brand(tvBrand)
                    .name(tvName.toUpperCase())
                    .screenType(screenType)
                    .tvLink(tvLink)
                    .imageLink(imageLink)
                    .diagonalByInch(diagonalByInch)
                    .diagonalByCm(diagonalByCm)
                    .resolution(resolution)
                    .price(price)
                    .discountInCashPayment(discount)
                    .active(true)
                    .smartTv(smartTv)
                    .wifiSupport(wifiSupport)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error happened when link " + tvLink + " was parsed. Error: " + ex.toString());
            return null;
        }

    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        log.info("KontaktHome service began to parse");
        getAllTvs();
        long endTime = System.currentTimeMillis();
        log.info("KontaktHome service ended to parse, spent time: " + ((float)(endTime - startTime) / 1000 / 60) + " minutes");
    }
}
