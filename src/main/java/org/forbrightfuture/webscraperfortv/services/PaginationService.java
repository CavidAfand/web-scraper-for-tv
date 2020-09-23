package org.forbrightfuture.webscraperfortv.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class PaginationService {

    private List<Integer> paginationList = new ArrayList<>();
    private boolean hasNextPage = true;
    private boolean hasPreviousPage = true;
    private int nextPageIndex;
    private int previousPageIndex;
    private int activePageIndex;
    private boolean previousDots = true;
    private boolean nextDots = true;


    private static PaginationService paginationService = new PaginationService();

    private PaginationService() {};

    private List<Integer> getPaginationList(int activePage, int allElements, int elementPerPage) {

        int lastPage = (allElements%elementPerPage!=0)?allElements/elementPerPage+1:allElements/elementPerPage;

        paginationList.clear();

        activePageIndex = activePage;

        if (activePage == 1) hasPreviousPage = false;
        else {
            hasPreviousPage = true;
            previousPageIndex = activePage - 1;
        }

        if (activePage == lastPage) hasNextPage = false;
        else {
            hasNextPage = true;
            nextPageIndex = activePage + 1;
        }

        if (activePage - 2 >= 1) {
            if (activePage - 2 != 1) previousDots = true;
            else previousDots = false;

            for (int i = activePage - 2; i <= activePage; i++) paginationList.add(i);
        }
        else {
            previousDots = false;
            for (int i = 1; i <= activePage; i++) paginationList.add(i);
        }

        if (activePage + 2 <= lastPage) {
            if (activePage + 2 != lastPage) nextDots = true;
            else nextDots = false;

            for (int i = activePage + 1; i <= activePage + 2; i++) paginationList.add(i);
        }
        else {
            nextDots = false;
            for (int i = activePage + 1; i <= lastPage; i++) paginationList.add(i);
        }


        return paginationList;
    }

    public static PaginationService getPagination(int activePage, int allElements, int elementPerPage) {
        paginationService.getPaginationList(activePage, allElements, elementPerPage);
        return paginationService;
    }

}
