package org.forbrightfuture.webscraperfortv.controllers;

import org.forbrightfuture.webscraperfortv.services.TvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ActionController.class)
public class ActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TvService tvService;

    @Test
    public void testFilterTvs() throws Exception {
        mockMvc.perform(get("/action/filter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSearchTv() throws Exception {
        mockMvc.perform(get("/action/search"))
                .andExpect(status().isBadRequest());
    }


}
