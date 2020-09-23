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
import java.util.Optional;

@Slf4j
@Service
public class IrshadElectronicsService implements Runnable {

    private Document document = null;
    private List<Tv> tvList = new ArrayList<>();

    public List<Tv> getTvList() {
        return tvList;
    }

    private List<Tv> getAllTvs() {
//        List<Tv> tvList = new ArrayList<>();
        int pageCount = 1;
        while (true) {
            try {
                document = Jsoup.connect("https://irshadelectronics.az/az/tv-ve-audio/televizorlar/?sort=1&p=" + (pageCount++)).get();
            }
            catch (IOException ex) {
                log.error("Error happened when html page https://irshadelectronics.az/az/tv-ve-audio/televizorlar/?sort=1&p=" + (pageCount-1) + " was loaded. Error: " + ex.toString());
            }

            List<Element> tvBlocksHtml = document.select("figure.pr_self");
            if (tvBlocksHtml.size() == 0) break;

            tvBlocksHtml.stream()
                    .map(i -> getTv("https://irshadelectronics.az" + i.selectFirst("a.full_link").attr("href")))
                    .forEach(i -> {
                        if (i != null)
                            tvList.add(i);
                    });
        }

        return tvList;
    }

    private Tv getTv(String url) {
        try {
            Document tvPageDocument = Jsoup.connect(url).get();
            // get tv name
            String tvName = tvPageDocument.selectFirst("h1 span").text().trim().toUpperCase();

            // get tv brand
            String tvBrand = tvPageDocument.selectFirst("header span.hidden_name span").text().trim().toUpperCase();

            // get discount
            Optional<Long> discountPrice = tvPageDocument.select("div.gallery_info div.discount").stream()
                    .filter(i -> i.attr("class").equals("discount"))
                    .map(i-> {
                        String discountString = i.selectFirst("strong").text();
                        long discount = Long.parseLong(discountString.substring(1, discountString.indexOf(' ')));
                        return discount;
                    })
                    .findFirst();

            // get price
            long price = Long.parseLong(tvPageDocument.selectFirst("div.acquiring_side div.price span").text());

            // get tv image link
            String imageLink = "https://irshadelectronics.az/" + tvPageDocument.selectFirst("ul#product_gallery li img").attr("src");

            HashMap<String, String> otherParams = new HashMap<>();
            List<Element> paramsHtml = tvPageDocument.select("div.param_list ul li");
            paramsHtml.stream()
                    .forEach(i -> {
                        String key = i.selectFirst("strong").text();
                        String value = i.selectFirst("span").text();
                        otherParams.put(key, value);
                    });

            // get screen type
            String screenType = getScreenTypeFromString(otherParams.get("Ekran növü:").trim().toUpperCase());

            // get tv diagonal
            String diagonalString = otherParams.get("Diaqonal:");
            int diagonalByInch;
            if (diagonalString.contains("\""))
                diagonalByInch = Integer.parseInt(otherParams.get("Diaqonal:").substring(0, otherParams.get("Diaqonal:").indexOf('\"')).trim());
            else
                diagonalByInch =    Integer.parseInt(otherParams.get("Diaqonal:").substring(0, otherParams.get("Diaqonal:").indexOf('d')).trim());
            int diagonalByCm = Math.round((float)diagonalByInch * 2.54f);

            // get tv resolution
            String resolutionString = getResolutionFromString(otherParams.get("Resolution:"));

            return Tv.builder().
                    brand(tvBrand)
                    .name(tvName.toUpperCase())
                    .tvLink(url)
                    .price(price)
                    .imageLink(imageLink)
                    .discountInCashPayment(discountPrice.isPresent()?discountPrice.get():0)
                    .diagonalByInch(diagonalByInch)
                    .diagonalByCm(diagonalByCm)
                    .screenType(screenType)
                    .resolution(resolutionString)
                    .wifiSupport(otherParams.get("Wi-Fi:").equalsIgnoreCase("yox")?false:true)
                    .smartTv(otherParams.get("Smart TV:").equalsIgnoreCase("yox")?false:true)
                    .active(true)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error happened when link " + url + " was parsed. Error: " + ex.toString());
            return null;
        }
    }

    private String getResolutionFromString(String resolutionFullString) {
        try {
            if (resolutionFullString.toLowerCase().contains("full hd")) return "1920x1080";
            if (resolutionFullString.toLowerCase().contains("hd")) return "1366x768";
            if (resolutionFullString.toLowerCase().contains("4k uhd")) return "3840x2160";
            if (resolutionFullString.toLowerCase().contains("8k uhd")) return "7680x4320";

            StringBuilder leftPartBuilder = new StringBuilder();
            StringBuilder rightPartBuilder = new StringBuilder();
            String leftPart, rightPart;
            boolean leftSpace = false, rightSpace = false;
            int indexX = resolutionFullString.indexOf('x');
            if (indexX == -1 ) indexX = resolutionFullString.indexOf('×');
            if (indexX == -1) return  null;

            int indexCopy = indexX;
            while (true) {
                indexCopy--;
                if (leftSpace == false) {
                    leftSpace = true;
                    if (Character.isWhitespace(resolutionFullString.charAt(indexCopy))) {
                        continue;
                    }
                }

                if (Character.isDigit(resolutionFullString.charAt(indexCopy))) {
                    leftPartBuilder.append(resolutionFullString.charAt(indexCopy));
                }
                else if (!Character.isDigit(resolutionFullString.charAt(indexCopy)))  {
                    leftPart = leftPartBuilder.reverse().toString();
                    break;
                }

                if (indexCopy == 0) {
                    leftPart = leftPartBuilder.reverse().toString();
                    break;
                }
            }

            indexCopy = indexX;
            while (true) {
                indexCopy++;
                if (rightSpace == false) {
                    rightSpace = true;
                    if (Character.isWhitespace(resolutionFullString.charAt(indexCopy))) {
                        continue;
                    }
                }

                if (Character.isDigit(resolutionFullString.charAt(indexCopy))) {
                    rightPartBuilder.append(resolutionFullString.charAt(indexCopy));
                }
                else if (!Character.isDigit(resolutionFullString.charAt(indexCopy))) {
                    rightPart = rightPartBuilder.toString();
                    break;
                }

                if (indexCopy == resolutionFullString.length()-1) {
                    rightPart = rightPartBuilder.toString();
                    break;
                }
            }

            return leftPart + "x"+ rightPart;

        }
        catch (Exception ex) {
            log.error("Error happened when resolution was got from string: " + resolutionFullString, ex);
            return resolutionFullString;
        }
    }

    private String getScreenTypeFromString(String screenTypeString) {
        if (screenTypeString.contains("NANOCELL")) return "NANOCELL";
        if (screenTypeString.contains("QLED")) return "QLED";
        if (screenTypeString.contains("OLED")) return "OLED";
        if (screenTypeString.contains("LED")) return "LED";
        if (screenTypeString.contains("TRILUMINOS")) return "TRILUMINOS";
        return screenTypeString.trim();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        log.info("Irshad service began to parse");
        getAllTvs();
        long endTime = System.currentTimeMillis();
        log.info("Irshad service ended to parse, spent time: " + ((float)(endTime - startTime) / 1000 / 60) + " minutes");
    }
}
