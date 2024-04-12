package com.inventory;

import lombok.Getter;

@Getter
public enum Tools {

    CHNS("Chainsaw", "Stihl"),
    LADW("Ladder", "Werner"),
    JAKD("Jackhammer", "DeWalt"),
    JAKR("Jackhammer", "Ridgid");

    private final String  type;
    private final String  brand;

     Tools(String type, String brand) {
       this.type = type;
       this.brand = brand;
     }
}
