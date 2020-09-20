package org.forbrightfuture.webscrapperfortv.services.websites;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KontaktHomeServiceTest {

    private KontaktHomeService kontaktHomeService = new KontaktHomeService();

    @Test
    void testRun() {
        kontaktHomeService.run();
        List<Tv> tvList = kontaktHomeService.getTvList();
        assertTrue(tvList.size() > 100);
    }
}