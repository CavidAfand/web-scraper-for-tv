package org.forbrightfuture.webscrapperfortv.controllers;

import org.forbrightfuture.webscrapperfortv.formModels.TvModel;
import org.forbrightfuture.webscrapperfortv.services.TvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("tvModel", new TvModel());
        return "index";
    }

    @PostMapping
    public String filterTvs(TvModel tvModel) {
        System.out.println(tvModel);
        return "index";
    }

}
