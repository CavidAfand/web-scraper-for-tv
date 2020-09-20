package org.forbrightfuture.webscrapperfortv.parallelTasks;

import lombok.extern.slf4j.Slf4j;
import org.forbrightfuture.webscrapperfortv.services.TvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateTvsService {

    private static final int UPDATE_INTERVAL_TIME_HOURS = 6;
    private static final long UPDATE_INTERVAL_TIME_MILLIS = UPDATE_INTERVAL_TIME_HOURS * 60 * 60 * 1000;

    @Autowired
    private TvService tvService;


    @Scheduled(fixedDelay = UPDATE_INTERVAL_TIME_MILLIS)
    public void updateTvList() {
        log.info("Database update began");

        // take start time of operation
        long startTime = System.currentTimeMillis();

        // update databse
//        tvService.updateTvList();

        // take end time of operation
        long endTime = System.currentTimeMillis();

        log.info("Tv database updated ended, time spent: " + (endTime - startTime) + " ms");
    }

}
