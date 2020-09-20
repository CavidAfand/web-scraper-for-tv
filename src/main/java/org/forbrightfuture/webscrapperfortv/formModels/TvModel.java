package org.forbrightfuture.webscrapperfortv.formModels;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
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
