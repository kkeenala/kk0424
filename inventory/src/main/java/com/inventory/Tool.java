package com.inventory;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Tool implements Serializable {

    private String type;
    private String brand;
    private String code;
    private double price;
    private String weekDayCharge;
    private String weekendCharge;
    private String holidayCharge;

    Tool(String type, String brand, String code, double price,
         String weekDayCharge, String weeekendCharge, String holidayCharge) {

        this.type = type;
        this.brand = brand;
        this.code = code;
        this.price = price;
        this.weekDayCharge = weekDayCharge;
        this.weekendCharge = weeekendCharge;
        this.holidayCharge = holidayCharge;
    }
}
