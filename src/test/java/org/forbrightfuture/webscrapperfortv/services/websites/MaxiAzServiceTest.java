package org.forbrightfuture.webscrapperfortv.services.websites;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxiAzServiceTest {

    private MaxiAzService maxiAzService = new MaxiAzService();

    @Test
    void testRun() {
        maxiAzService.run();
        List<Tv> tvList = maxiAzService.getTvList();
        assertTrue(tvList.size() > 100);
    }
}