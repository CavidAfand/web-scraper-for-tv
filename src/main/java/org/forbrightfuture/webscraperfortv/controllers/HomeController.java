package org.forbrightfuture.webscraperfortv.controllers;

import org.forbrightfuture.webscraperfortv.entities.Tv;
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
@RequestMapping("/")
public class HomeController {

    @Autowired
    private TvService tvService;

    @GetMapping
    public String goIndexPage(Model model) {
        model.addAttribute("brandList", tvService.getAllDistinctBrands());
        model.addAttribute("resolutionList", tvService.getAllDistinctResolutions());
        model.addAttribute("screenTypeList", tvService.getAllDistinctScreenTypes());
        return "index";
    }

    @GetMapping("/tvs")
    public String sortTvs(@RequestParam(name = "mode", required = false) Integer mode,
                          @RequestParam(name = "page", required = false) Integer page,
                          Model model) {

        // check request param is valid or not
        if (page == null) page = 1;
        if (mode == null) mode = 1;

        List<Tv> tvList = new ArrayList<>();

        // get number of tvs
        int numberOfTvs = tvService.getNumberOfTvs();

        // check correctness of page argument
        if (page < 1) page = 1;

        // check mode and get tvs
        if (mode == 1)
            tvList = tvService.getTvsFromCheapToExpensive(page);
        else if (mode == 2)
            tvList = tvService.getTvsFromExpensiveToCheap(page);

        // add some data to model attribute
        model.addAttribute("pagination", PaginationService.getPagination(page, numberOfTvs, tvService.getTvElementPerPage()));
        model.addAttribute("pageMode", mode);
        model.addAttribute("tvList", tvList);

        return "tv_list";
    }

}
