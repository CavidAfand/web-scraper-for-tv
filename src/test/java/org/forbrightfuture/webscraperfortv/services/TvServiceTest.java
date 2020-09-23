package org.forbrightfuture.webscraperfortv.services;



import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.forbrightfuture.webscraperfortv.formModels.TvModel;
import org.forbrightfuture.webscraperfortv.repos.TvRepository;
import org.forbrightfuture.webscraperfortv.services.websites.BakuElectronicsService;
import org.forbrightfuture.webscraperfortv.services.websites.IrshadElectronicsService;
import org.forbrightfuture.webscraperfortv.services.websites.KontaktHomeService;
import org.forbrightfuture.webscraperfortv.services.websites.MaxiAzService;
import org.junit.Test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


//@RunWith(SpringRunner.class)
public class TvServiceTest {

    @Mock
    private IrshadElectronicsService irshadElectronicsService;
    @Mock
    private KontaktHomeService kontaktHomeService;
    @Mock
    private BakuElectronicsService bakuElectronicsService;
    @Mock
    private MaxiAzService maxiAzService;
    @Mock
    private TvRepository tvRepository;

    @MockBean
    Pageable pageable;


    private TvService tvService = null;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tvService = new TvService(irshadElectronicsService, kontaktHomeService, bakuElectronicsService, maxiAzService, tvRepository);
    }

    @Test
    public void testGetNumberOfTvs() {
        when(tvRepository.getNumberOfTvs()).thenReturn(55);
        int numberOfTvs = tvService.getNumberOfTvs();
        assertEquals(55, numberOfTvs);
        verify(tvRepository).getNumberOfTvs();
    }

    @Test
    public void getAllDistinctBrands() {
        when(tvRepository.getDistinctBrandsOfActiveTvs()).thenReturn(new ArrayList<>(Arrays.asList("Samsung","LG","AIWA")));
        List<String> brandList = tvService.getAllDistinctBrands();
        assertEquals(Arrays.asList("Samsung","LG","AIWA"), brandList);
        verify(tvRepository).getDistinctBrandsOfActiveTvs();
    }

    @Test
    public void getAllDistinctResolutions() {
        when(tvRepository.getDistinctResolutionsOfActiveTvs()).thenReturn(new ArrayList<>(Arrays.asList("1366x768","1920x1080","3840x2160","7680x4320")));
        List<String> resolutionList = tvService.getAllDistinctResolutions();
        assertEquals(Arrays.asList("1366x768","1920x1080","3840x2160","7680x4320"), resolutionList);
        verify(tvRepository).getDistinctResolutionsOfActiveTvs();
    }

    @Test
    public void getAllDistinctScreenTypes() {
        when(tvRepository.getDisinctScreenTypesOfActiveTvs()).thenReturn(new ArrayList<>(Arrays.asList("LED","NANOCELL","OLED","QLED")));
        List<String> screenTypeList = tvService.getAllDistinctScreenTypes();
        assertEquals(Arrays.asList("LED","NANOCELL","OLED","QLED"), screenTypeList);
        verify(tvRepository).getDisinctScreenTypesOfActiveTvs();
    }

    @Test
    public void getNumberOfFoundTvs() {
        when(tvRepository.getNumberOfFoundTvs(Mockito.any(String.class))).thenReturn(5);
        int number = tvService.getNumberOfFoundTvs("LG");
        assertEquals(5, number);
    }


    @Test
    public void getNumberofFilteredTvs() {
        when(tvRepository.findNumberOfFilteredTvs(Mockito.any(TvModel.class))).thenReturn((long)0);
        long number = tvService.getNumberofFilteredTvs(null);
        assertEquals(0, number);
    }

}