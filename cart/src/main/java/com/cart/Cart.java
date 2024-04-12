package com.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {

    private String cartId;
    List<Item> items = new ArrayList<>();
}
