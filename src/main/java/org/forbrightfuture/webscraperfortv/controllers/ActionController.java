package org.forbrightfuture.webscraperfortv.controllers;


import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.forbrightfuture.webscraperfortv.formModels.TvModel;
import org.forbrightfuture.webscraperfortv.services.PaginationService;
import org.forbrightfuture.webscraperfortv.services.TvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/action")
public class ActionController {

    @Autowired
    private TvService tvService;

    @GetMapping("/filter")
    public String filterTvs(@RequestParam(name = "page" , required = false) Integer page,
                            @RequestParam(name = "startPrice") Integer startPrice,
                            @RequestParam(name = "endPrice") Integer endPrice,
                            @RequestParam(name = "startDiagonal") Integer startDiagonal,
                            @RequestParam(name = "endDiagonal") Integer endDiagonal,
                            @RequestParam(name = "brand", required = false) List<String> brandList,
                            @RequestParam(name = "screenType", required = false) List<String> screenTypeList,
                            @RequestParam(name = "resolution", required = false) List<String> resolutionList,
                            @RequestParam(name = "smartTv", required = false) String smartTv,
                            @RequestParam(name = "wifi", required = false) String wifi,
                            Model model) {

        TvModel tvModel = TvModel.builder()
                .startPrice(startPrice)
                .endPrice(endPrice)
                .startDiagonal(startDiagonal)
                .endDiagonal(endDiagonal)
                .screenType(screenTypeList != null? screenTypeList : new ArrayList<>())
                .resolution(resolutionList != null? resolutionList : new ArrayList<>())
                .brand(brandList != null? brandList : new ArrayList<>())
                .smartTv(smartTv)
                .wifi(wifi)
                .build();

        // check page
        if (page == null || page < 1) page = 1;

        // filter and find filtered tvs
        List<Tv> tvList = tvService.filterTvs(tvModel, page);

        // number of all found tvs
        int numberOfTvs = tvService.getNumberofFilteredTvs(tvModel);

        // add some data to models
        model.addAttribute("pagination", PaginationService.getPagination(page, numberOfTvs, tvService.getTvElementPerPage()));
        model.addAttribute("tvList", tvList);
        model.addAttribute("linkPart", generateStringForLink(tvModel));
        model.addAttribute("numberOfAllFoundTvs", numberOfTvs);
        return "tv_filter";
    }

    @GetMapping("/search")
    public String searchTv(@RequestParam("search") String searchString,
                           @RequestParam(name = "page", required = false) Integer id,
                           Model model) {

        // check id parameter is valid or not
        if (id == null || id < 1) id = 1;

        // check search string
        if (searchString.toLowerCase().contains("televizor")) searchString.replace("televizor", "").trim();

        // found tvs
        List<Tv> tvList = tvService.findTvsByName(searchString.toUpperCase(), id);

        // number of all found tvs
        int numberOfTvs = tvService.getNumberOfFoundTvs(searchString);

        // add some data to models
        model.addAttribute("pagination", PaginationService.getPagination(id, numberOfTvs, tvService.getTvElementPerPage()));
        model.addAttribute("tvList", tvList);
        model.addAttribute("searchString", searchString);
        model.addAttribute("numberOfAllFoundTvs", numberOfTvs);
        return "tv_search";
    }


    /**
     * This method used for generating particular part of link which is used in pagination in filter page
     **/
    private String generateStringForLink(TvModel tvModel) {

        StringBuilder linkPart = new StringBuilder();
        linkPart.append("&startPrice=").append(tvModel.getStartPrice())
                .append("&endPrice=").append(tvModel.getEndPrice())
                .append("&startDiagonal=").append(tvModel.getStartDiagonal())
                .append("&endDiagonal=").append(tvModel.getEndDiagonal());

        tvModel.getBrand().stream().forEach(i -> linkPart.append("&brand=").append(i));
        tvModel.getResolution().stream().forEach(i -> linkPart.append("&resolution=").append(i));
        tvModel.getScreenType().stream().forEach(i -> linkPart.append("&screenType=").append(i));

        if (tvModel.getWifi() != null) linkPart.append("&wifi=").append("wifi");
        if (tvModel.getSmartTv() != null) linkPart.append("&smartTv=").append("smartTv");

        return linkPart.toString();
    }
}
