package org.forbrightfuture.webscraperfortv.repos;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TvRepository extends JpaRepository<Tv, String>, TvRepositoryCustom {

    @Query("SELECT distinct brand from Tv t where t.active = true")
    List<String> getDistinctBrandsOfActiveTvs();

    @Query("SELECT distinct resolution from Tv t where t.active = true")
    List<String> getDistinctResolutionsOfActiveTvs();

    @Query("select distinct screenType from Tv t where t.active = true")
    List<String> getDisinctScreenTypesOfActiveTvs();

    @Query("select t from Tv t where t.active = true order by t.price asc")
    List<Tv> getTvsByOrderByPriceAsc(Pageable page);

    @Query("select t from Tv t where t.active = true order by t.price desc")
    List<Tv> getTvsByOrderByPriceDesc(Pageable page);

    @Query("select count(t) from Tv t where t.active = true")
    int getNumberOfTvs();

    @Query("select t from Tv t where t.active = true and t.name like concat('%',:tvName,'%') order by t.price asc")
    List<Tv> findTvsByNameLike(@Param("tvName") String tvName, Pageable pageable);

    @Query("select count(t) from Tv t where t.active = true and t.name like concat('%',:tvName,'%')")
    int getNumberOfFoundTvs(@Param("tvName") String tvName);
}
