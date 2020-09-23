package org.forbrightfuture.webscraperfortv.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name="tv_table")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tv {

    @Id
    private String tvLink;

    @Column(name = "brand", length = 100, nullable = false)
    private String  brand;

    @Column(name = "name", length = 187)
    private String name;

    @Column(name="screen_type", length = 50)
    private String screenType;  // LED,LCD

    @Column(name="diagonal_inch")
    private int diagonalByInch; // 62"

    @Column(name="diagonal_cm")
    private int diagonalByCm; // 121cm

    @Column(name="resolution", length = 70)
    private String resolution;

    @Column(name="wifi_support")
    private boolean wifiSupport;

    @Column(name = "smart_tv")
    private boolean smartTv;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "discount_cash_payment")
    private float discountInCashPayment;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "active", nullable = false)
    private boolean active;

}
