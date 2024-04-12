package com.cart;

import com.inventory.Tools;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class CartService {

    Map<String, Cart> cartMap;

    {
        cartMap = new HashMap<>();
    }


    public Cart adjustCart(String toolCode, int quantity,  String userId, String operation) {

        Cart cart = null;

        if (!Objects.isNull(cartMap.get(userId))) {
            cart = cartMap.get(userId);
        } else {
            cart = new Cart();

        }

        return adjustItems(toolCode, quantity, userId, cart, operation);

    }


    private Cart adjustItems(String toolCode, int quantity, String userId, Cart cart, String operation) {

        List<Item> cartItems = cart.getItems();
        boolean foundItem = false;

        if (!cartItems.isEmpty()) {
            for (Item itemsCart : cartItems) {

                if (itemsCart.getToolCode().equals(toolCode)) {

                    if(operation.isEmpty() ||
                            operation.isBlank() ||
                            operation.equalsIgnoreCase("add")
                             )
                      itemsCart.setQuantity(itemsCart.getQuantity() + quantity);

                    else
                        itemsCart.setQuantity(itemsCart.getQuantity() - quantity);

                    cartMap.put(userId, cart);
                    foundItem = true;
                    break;
                }
            }

            if(!foundItem)
                cartMap.put(userId, addNewItemCart(toolCode, quantity, cart));



        } else {
              cartMap.put(userId, addNewItemCart(toolCode, quantity, cart));

        }

        foundItem = false; // reset
        return cart;

    }

    public Cart getCart(String userId) throws Exception {

        Cart cart = cartMap.get(userId);

        if(Objects.isNull(cart))
            throw new Exception();

        return cart;
    }

    private Cart addNewItemCart(String toolCode, int quantity, Cart existingCart) {

        Cart cart = null;

        if (Objects.isNull(existingCart))
            cart = new Cart();
        else
            cart = existingCart;

        Item item = new Item();
        item.setToolCode(toolCode);
        item.setToolType(Tools.valueOf(toolCode).getType());
        item.setQuantity(quantity);

        cart.getItems().add(item);

        return cart;
    }

    public Cart deleteItem(String toolCode,  String userId) throws Exception {

        Cart cart = null;
        boolean foundItem = false;

        if (!Objects.isNull(cartMap.get(userId))) {
            cart = cartMap.get(userId);
            List<Item> cartItems = cart.getItems();

            if (!cartItems.isEmpty()) {
                for (Item itemsCart : cartItems) {

                    if (itemsCart.getToolCode().equals(toolCode)) {
                        cartItems.remove(itemsCart);
                        foundItem = true;
                        break;
                    }
                }
            }
        }

        if(!foundItem)
            throw new Exception();

        return cart;
    }

    public void deleteCart(String userId) throws Exception {

        if(!Objects.isNull(cartMap.get(userId)))
            cartMap.remove(userId);

        else
            throw new Exception();
    }

}

