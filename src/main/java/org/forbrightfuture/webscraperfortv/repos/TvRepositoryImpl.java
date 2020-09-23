package org.forbrightfuture.webscraperfortv.repos;

import org.forbrightfuture.webscraperfortv.entities.Tv;
import org.forbrightfuture.webscraperfortv.formModels.TvModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class TvRepositoryImpl implements TvRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tv> filterTvs(TvModel tvModel, int page, int elementPerPage) {

        // creating query string
        StringBuilder queryString = new StringBuilder("select t from Tv t where t.active = true ");

        queryString.append(generateWherePartOfQuery(tvModel));

        Query query = em.createQuery(queryString.toString(), Tv.class)
                .setFirstResult(page * elementPerPage)
                .setMaxResults(elementPerPage);

        return completeQuery(tvModel, query).getResultList();
    }

    @Override
    public long findNumberOfFilteredTvs(TvModel tvModel) {
        StringBuilder queryString = new StringBuilder("select count(t) from org.forbrightfuture.webscraperfortv.entities.Tv t where t.active = true ");

        queryString.append(generateWherePartOfQuery(tvModel));

        Query query = em.createQuery(queryString.toString(), Long.class);

        return (long) completeQuery(tvModel, query).getResultList().get(0);
    }

    private String generateWherePartOfQuery(TvModel tvModel) {

        StringBuilder queryString = new StringBuilder();

        // it is used in query string for indexing parameters
        int count = 1;

        // if tv brands exist, add them to query string
        if (tvModel.getBrand().size() > 0) {
            queryString.append(" and t.brand in (");
            for (int i=0; i < tvModel.getBrand().size(); i++) {
                if (i != tvModel.getBrand().size()-1) queryString.append("?" + (count++) + ",");
                else queryString.append("?" + (count++) + ")");
            }
        }

        // if screen types exist, add them to query string
        if (tvModel.getScreenType().size() > 0) {
            queryString.append(" and t.screenType in (");
            for (int i=0; i < tvModel.getScreenType().size(); i++) {
                if (i != tvModel.getScreenType().size()-1) queryString.append("?" + (count++) + ",");
                else queryString.append("?" + (count++) + ")");
            }
        }

        // if resolutions exist, add them to query string
        if (tvModel.getResolution().size() > 0) {
            queryString.append(" and t.resolution in (");
            for (int i=0; i < tvModel.getResolution().size(); i++) {
                if (i != tvModel.getResolution().size()-1) queryString.append("?" + (count++) + ",");
                else queryString.append("?" + (count++) + ")");
            }
        }

        // if wifi exists, add it to to query string
        if (tvModel.getWifi() != null) queryString.append(" and t.wifiSupport = true ");

        // if smart tv exists, add it to to query string
        if (tvModel.getSmartTv() != null) queryString.append(" and t.smartTv = true");

        // add price range to query string
        queryString.append(" and t.price > " + tvModel.getStartPrice() + " and t.price < " + tvModel.getEndPrice() + " ");
        // add diagonal range to query string
        queryString.append(" and t.diagonalByCm > " + tvModel.getStartDiagonal() + " and t.diagonalByCm < " + tvModel.getEndDiagonal());

        // sort by price
        queryString.append(" order by t.price asc");

        return queryString.toString();
    }

    private Query completeQuery(TvModel tvModel, Query query) {
        int count = 1;
        for (int i=0; i < tvModel.getBrand().size(); i++) {
            query.setParameter(count, tvModel.getBrand().get(i));
            count++;
        }

        for (int i=0; i < tvModel.getScreenType().size(); i++) {
            query.setParameter(count, tvModel.getScreenType().get(i));
            count++;
        }

        for (int i=0; i < tvModel.getResolution().size(); i++) {
            query.setParameter(count, tvModel.getResolution().get(i));
            count++;
        }

        return query;
    }


}
