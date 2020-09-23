package org.forbrightfuture.webscraperfortv.services.websites;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BakuElectronicsServiceTest {

    private BakuElectronicsService bakuElectronicsService = new BakuElectronicsService();

    @Test
    void testRun() {
        bakuElectronicsService.run();
        List<Tv> tvList = bakuElectronicsService.getTvList();
        System.out.println("Size: " + tvList.size());
        assertTrue(tvList.size() > 100);
    }
}