package org.forbrightfuture.webscrapperfortv.services.websites;

import lombok.extern.slf4j.Slf4j;
import org.forbrightfuture.webscrapperfortv.entities.Tv;
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
public class MaxiAzService implements Runnable{

    private List<Tv> tvList = new ArrayList<>();

    public List<Tv> getTvList() {
        return tvList;
    }

    private List<Tv> getAllTvs() {
        String url = "https://maxi.az/televizorlar-ve-audio-video/televizorlar/?gclid=CjwKCAjwzIH7BRAbEiwAoDxxTnUb8ctzxxNvm3wmWtplhFFviRZEbR8pnTJ9ZjMeczr4UhBEuXoR0RoCT3cQAvD_BwE&PAGEN_100=";
        int page = 1;

        while (true) {
            try {
                Document document = Jsoup.connect(url + String.valueOf(page)).get();

                int activeIndex = Integer.parseInt(document.selectFirst("div.pagi ul li a.act").text());
                if (activeIndex != page) {
                    break;
                }
                page++;

                List<Element> tvBlockHtml = document.select("div.cont-cat div.cont-cat-in div.one-cat-item");
                tvBlockHtml.stream()
                        .map(i -> getTv("https://maxi.az" + i.selectFirst("div.one-cat-item-in div.one-cat-item-tit a").attr("href")))
                        .forEach(i -> {
                            if (i != null) tvList.add(i);
                        });



            }
            catch (IOException ex) {
                log.error("Error happened when page " + url + page  + " was parsed", ex);
            }
        }

        return tvList;
    }

    private Tv getTv(String tvLink) {
        try {
            Document tvPageHtml = Jsoup.connect(tvLink).get();

            Element tableElement = tvPageHtml.selectFirst("main.wrap div.wrap-in div.page div.char-wrap div.char-atr-in table");
            List<Element> tableRowElements = tableElement.getElementsByTag("tr");

            HashMap<String, String> params = new HashMap<>();

            for (int i=0; i<tableRowElements.size(); i++) {
                List<Element> colElements = tableRowElements.get(i).select("td");
                params.put(colElements.get(0).text(), colElements.get(1).text());
            }

            // get Tv brand
//            tv.setBrand(params.get("Brend").trim().toUpperCase());
            String tvBrand = params.get("Brend").trim().toUpperCase();

            // get tv name
            String tvName = tvPageHtml.selectFirst("body div.wrapper main.wrap div.wrap-in div.page  div.product div.char-prod div.fix-info div.fix-tit span").text().trim();

            // get tv image link
            String imageLink = "https://maxi.az" + tvPageHtml.selectFirst("body div.wrapper main.wrap div.wrap-in div.page div.product div.about-prod div.slid-prod div.slid-wrap div.slid-wrap-in img").attr("src");

            // get tv price
            float price = getPriceFromString(tvPageHtml.selectFirst("body div.wrapper main.wrap div.wrap-in div.about-prod div.prod-comp div.price-prod div.price-prod-now span").text());

            // get Tv screen type
            String screenType = null;
            if (params.get("Televizor növü") != null)
//                tv.setScreenType(params.get("Televizor növü").trim().toUpperCase());
                screenType = params.get("Televizor növü").trim().toUpperCase();

            // get Tv diagonal by inch
//            tv.setDiagonalByInch(getDiagonalFromString(params.get("Ekran diaqonalı, (düym)").trim().toUpperCase()));
            int diagonalByInch = getDiagonalFromString(params.get("Ekran diaqonalı, (düym)").trim().toUpperCase());

            // get Tv diagonal by cm
            int diagonalByCm = Math.round((float)diagonalByInch * 2.54f);

            // get Tv resolution
//            tv.setResolution(getResolutionFromString(params.get("HD ölçüləri")));
            String tvResolution = getResolutionFromString(params.get("HD ölçüləri"));

            // check wifi support
            boolean wifiSupport = false;
            if (params.get("Wi-Fi") != null)
                wifiSupport = !params.get("Wi-Fi").equalsIgnoreCase("yox");

            // check smart Tv functionality
            boolean smartTv = false;
            if (params.get("Smart TV") != null)
                smartTv = !params.get("Smart TV").equalsIgnoreCase("yox");

            return Tv.builder()
                    .brand(tvBrand)
                    .name(tvName)
                    .resolution(tvResolution)
                    .screenType(screenType)
                    .discountInCashPayment(0.0f)
                    .tvLink(tvLink)
                    .imageLink(imageLink)
                    .price(price)
                    .diagonalByInch(diagonalByInch)
                    .diagonalByCm(diagonalByCm)
                    .wifiSupport(wifiSupport)
                    .smartTv(smartTv)
                    .active(true)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error happened when " + tvLink + " was parsed", ex);
            return null;
        }
    }

    private float getPriceFromString(String price) {
        return Float.parseFloat(price.trim().replaceAll("\\s+",""));
    }

    private String getResolutionFromString(String resolutionString) {
        resolutionString = resolutionString.replace("*", "x");
        resolutionString = resolutionString.replace("\\s+","");
        resolutionString = resolutionString.replace(" ", "");

        if (resolutionString.indexOf('(') != -1 && resolutionString.indexOf('(') < resolutionString.indexOf('x'))
            return resolutionString.substring(resolutionString.indexOf('(')+1,resolutionString.indexOf(')')).trim().replace("\\s+","");
        if (resolutionString.toLowerCase().contains("full hd")) return "1920x1080";
        if (resolutionString.toLowerCase().contains("8k uhd")) return "1366x768";
        if (resolutionString.toLowerCase().contains("4k uhd")) return "3840x2160";
        if (resolutionString.toLowerCase().contains("hd")) return "7680x4320";
        return resolutionString;
    }

    private int getDiagonalFromString(String diagonalString) {
        if (diagonalString.indexOf('*') != -1) return 0;
        int charIndex = diagonalString.indexOf('"');
        if (charIndex == -1) charIndex = diagonalString.indexOf('\'');
        if (charIndex == -1) charIndex = diagonalString.indexOf(' ');
        if (charIndex == -1) return Integer.parseInt(diagonalString);
        return Integer.parseInt(diagonalString.substring(0, charIndex));
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        log.info("MaxiAz service began to parse");
        getAllTvs();
        long endTime = System.currentTimeMillis();
        log.info("MaxiAz service ended to parse, spent time: " + (endTime - startTime));
    }
}
