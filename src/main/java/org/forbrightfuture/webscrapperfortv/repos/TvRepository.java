package org.forbrightfuture.webscrapperfortv.repos;

import org.forbrightfuture.webscrapperfortv.entities.Tv;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TvRepository extends JpaRepository<Tv, String> {

    @Query("SELECT distinct brand from Tv t where t.active = true")
    List<String> getDistinctBrandsOfActiveTvs();

    @Query("SELECT distinct resolution from Tv t where t.active = true")
    List<String> getDistinctResolutionsOfActiveTvs();

    @Query("select distinct diagonalByInch from Tv t where t.active = true")
    List<Integer> getDistinctDiagonalsByInchOfActiveTvs();

    @Query("select distinct screenType from Tv t where t.active = true")
    List<String> getDisinctScreenTypesOfActiveTvs();

    List<Tv> getTvsByOrderByPriceAsc(Pageable page);
}
