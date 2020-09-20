package org.forbrightfuture.webscrapperfortv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebScrapperForTvApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebScrapperForTvApplication.class, args);
    }

}
