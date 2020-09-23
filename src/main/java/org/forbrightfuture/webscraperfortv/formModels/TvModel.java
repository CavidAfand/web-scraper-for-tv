package org.forbrightfuture.webscraperfortv.formModels;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class TvModel {

    private List<String> brand;
    private List<String> resolution;
    private List<String> screenType;
    private int startPrice;
    private int endPrice;
    private int startDiagonal;
    private int endDiagonal;
    private String wifi;
    private String smartTv;

}
