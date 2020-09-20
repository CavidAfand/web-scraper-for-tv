package org.forbrightfuture.webscrapperfortv.controllers;

import org.forbrightfuture.webscrapperfortv.formModels.TvModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/action")
public class ActionController {

    @PostMapping("/filter")
    public String filterTvs(TvModel tvModel) {
        System.out.println(tvModel);
        return null;
    }

    @PostMapping("/search")
    public String searchTv(String searchString) {

        return null;
    }
}
