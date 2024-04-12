package com.orders;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Order {

    private String toolCode;
    private String toolType;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private String preDiscountCharge;
    private String discountPercent;
    private String discountedAmount;
    private String finalCharge;

}
