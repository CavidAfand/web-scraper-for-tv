package org.forbrightfuture.webscraperfortv.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ErrorController.class)
public class MyErrorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void handleError() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk());
    }

}
