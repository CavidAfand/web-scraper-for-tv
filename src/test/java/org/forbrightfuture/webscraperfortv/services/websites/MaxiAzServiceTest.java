package org.forbrightfuture.webscraperfortv.services.websites;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxiAzServiceTest {

    private MaxiAzService maxiAzService = new MaxiAzService();

    @Test
    void testRun() {
        maxiAzService.run();
        List<Tv> tvList = maxiAzService.getTvList();
        System.out.println("Size: " + tvList.size());
        assertTrue(tvList.size() > 100);
    }
}