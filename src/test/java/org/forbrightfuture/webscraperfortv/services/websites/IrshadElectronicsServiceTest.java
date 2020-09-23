package org.forbrightfuture.webscraperfortv.services.websites;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IrshadElectronicsServiceTest {

    private IrshadElectronicsService irshadElectronicsService = new IrshadElectronicsService();


    @Test
    void testRun() {
        irshadElectronicsService.run();
        List<Tv> tvList = irshadElectronicsService.getTvList();
        System.out.println("Size: " + tvList.size());
        assertTrue(tvList.size() > 100);
    }

}