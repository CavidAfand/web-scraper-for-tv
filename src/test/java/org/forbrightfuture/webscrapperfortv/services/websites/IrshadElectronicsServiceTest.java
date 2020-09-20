package org.forbrightfuture.webscrapperfortv.services.websites;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IrshadElectronicsServiceTest {

    private IrshadElectronicsService irshadElectronicsService = new IrshadElectronicsService();

    @Test
    void testRun() {
        irshadElectronicsService.run();
        List<Tv> tvList = irshadElectronicsService.getTvList();
        assertTrue(tvList.size() > 100);
    }

}