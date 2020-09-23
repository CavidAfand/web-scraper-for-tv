package org.forbrightfuture.webscraperfortv.repos;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.forbrightfuture.webscraperfortv.formModels.TvModel;

import java.util.List;

public interface TvRepositoryCustom {

    List<Tv> filterTvs(TvModel tvModel, int page, int elementPerPage);

    long findNumberOfFilteredTvs(TvModel tvModel);
}
