package org.forbrightfuture.webscrapperfortv.services;

import lombok.extern.slf4j.Slf4j;
import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.forbrightfuture.webscrapperfortv.repos.TvRepository;
import org.forbrightfuture.webscrapperfortv.services.websites.BakuElectronicsService;
import org.forbrightfuture.webscrapperfortv.services.websites.IrshadElectronicsService;
import org.forbrightfuture.webscrapperfortv.services.websites.KontaktHomeService;
import org.forbrightfuture.webscrapperfortv.services.websites.MaxiAzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class TvService {

    private IrshadElectronicsService irshadElectronicsService;
    private KontaktHomeService kontaktHomeService;
    private BakuElectronicsService bakuElectronicsService;
    private MaxiAzService maxiAzService;
    private TvRepository tvRepository;

    @Value("${tv.view.number-per-page}")
    private int numberPerPage;

    @Autowired
    public TvService(IrshadElectronicsService irshadElectronicsService,
                     KontaktHomeService kontaktHomeService,
                     BakuElectronicsService bakuElectronicsService,
                     MaxiAzService maxiAzService,
                     TvRepository tvRepository) {
        this.irshadElectronicsService = irshadElectronicsService;
        this.kontaktHomeService = kontaktHomeService;
        this.bakuElectronicsService = bakuElectronicsService;
        this.maxiAzService = maxiAzService;
        this.tvRepository = tvRepository;
    }

    public List<String> getAllDistinctBrands() {
        return tvRepository.getDistinctBrandsOfActiveTvs();
    }
    public List<String> getAllDistinctResolutions() {
        return tvRepository.getDistinctResolutionsOfActiveTvs();
    }
    public List<String> getAllDistinctScreenTypes() {
        return tvRepository.getDisinctScreenTypesOfActiveTvs();
    }

    public List<Tv> getTvsFromCheapToExpensive(int page) {
        List<Tv> tvList = tvRepository.getTvsByOrderByPriceAsc(PageRequest.of(page, numberPerPage));
        return tvList;
    }

    private List<Tv> getAllTvFromWebsites() {
        List<Tv> tvList = new ArrayList<>();

        // creating threads for services
        Thread irshadElectronicsThread = new Thread(irshadElectronicsService);
        Thread kontaktHomeThread = new Thread(kontaktHomeService);
        Thread bakuElectronicsThread = new Thread(bakuElectronicsService);
        Thread maxiAzThread = new Thread(maxiAzService);

        // start threads to parse tv pages
        irshadElectronicsThread.start();
        kontaktHomeThread.start();
        bakuElectronicsThread.start();
        maxiAzThread.start();

        // wait until all threads die
        while (irshadElectronicsThread.isAlive() ||
               kontaktHomeThread.isAlive() ||
               bakuElectronicsThread.isAlive() ||
               maxiAzThread.isAlive());

        // add all tvs to list (tvList)
        irshadElectronicsService.getTvList().stream().forEach(i->tvList.add(i));
        kontaktHomeService.getTvList().stream().forEach(i -> tvList.add(i));
        bakuElectronicsService.getTvList().stream().forEach(i -> tvList.add(i));
        maxiAzService.getTvList().stream().forEach(i -> tvList.add(i));

        return tvList;
    }

    // TODO Scheduling-de cagirilan methoddur, database yenilemelidi
    @Transactional
    public void updateTvList() {

        // get all tvs from db
        List<Tv> allTvsInDatabase = tvRepository.findAll();

        // make all tvs in database inactive
        if (allTvsInDatabase != null)
            allTvsInDatabase.stream().forEach(i -> {
                i.setActive(false);
                tvRepository.save(i);
            });

        // get all tvs from websites
        List<Tv> allNewTvs = getAllTvFromWebsites();

        // update old tvs and save new tvs
        allNewTvs.stream().forEach(i -> tvRepository.save(i));
    }
}
