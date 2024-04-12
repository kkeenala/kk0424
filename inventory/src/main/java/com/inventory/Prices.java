package com.inventory;

import lombok.Getter;

@Getter
public enum Prices {

    Ladder(1.99, "Yes", "Yes", "No"),
    Chainsaw(1.49, "Yes", "No", "Yes"),
    Jackhammer(2.99, "Yes", "No", "No");


    private final double  price;
    private final String  weekdayCharge;
    private final String  weekendCharge;
    private final String  holidayCharge;

     Prices(double price, String weekdayCharge, String  weekendCharge, String  holidayCharge) {
       this.price = price;
       this.weekdayCharge = weekdayCharge;
       this.weekendCharge = weekendCharge;
       this.holidayCharge = holidayCharge;
     }
}
