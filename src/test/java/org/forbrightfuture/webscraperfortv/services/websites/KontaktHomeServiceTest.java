package org.forbrightfuture.webscraperfortv.services.websites;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KontaktHomeServiceTest {

    private KontaktHomeService kontaktHomeService = new KontaktHomeService();

    @Test
    void testRun() {
        kontaktHomeService.run();
        List<Tv> tvList = kontaktHomeService.getTvList();
        System.out.println("List size: " + tvList.size());
        assertTrue(tvList.size() > 100);
    }
}