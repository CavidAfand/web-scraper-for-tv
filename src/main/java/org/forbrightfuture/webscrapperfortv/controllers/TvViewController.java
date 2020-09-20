package org.forbrightfuture.webscrapperfortv.controllers;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.forbrightfuture.webscrapperfortv.services.TvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TvViewController {

    @Autowired
    private TvService tvService;

    @GetMapping("/tvs")
    public String sortTvsFromCheapToExpensive(@RequestParam("mode") Integer mode, @RequestParam("page") Integer page, Model model) {
        if (mode == 1) {
            List<Tv> tvList = tvService.getTvsFromCheapToExpensive(page);
            model.addAttribute("tvList", tvList);
            model.addAttribute("pageMode", mode);
            return "tv_list";
        }
        return null;
    }

}
