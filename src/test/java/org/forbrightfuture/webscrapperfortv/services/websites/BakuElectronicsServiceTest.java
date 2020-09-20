package org.forbrightfuture.webscrapperfortv.services.websites;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BakuElectronicsServiceTest {

    private BakuElectronicsService bakuElectronicsService = new BakuElectronicsService();

    @Test
    void testRun() {
        bakuElectronicsService.run();
        List<Tv> tvList = bakuElectronicsService.getTvList();
        assertTrue(tvList.size() > 100);
    }
}