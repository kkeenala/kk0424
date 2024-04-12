package com.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @RequestMapping(value = "/createAdjustCart", method = RequestMethod.PUT)
    public ResponseEntity<?> addItemToCart(@RequestParam String code, @RequestParam int quantity,
                                       @RequestParam String userId,  @RequestParam String operation) {
        try {
            return ResponseEntity.ok(cartService.adjustCart(code, quantity,  userId, operation));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Tool with the code " +code+ " doesn't exists in our inventory.");
        }
    }

    @RequestMapping(value = "/getCart", method = RequestMethod.GET)
    public ResponseEntity<?> getCart(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(cartService.getCart(userId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("There is no cart for the user " +userId+ " .");
        }
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.PUT)
    public ResponseEntity<?> deleteItemInCart(@RequestParam String code,
                                       @RequestParam String userId) {
        try {
            return ResponseEntity.ok(cartService.deleteItem(code,  userId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Tool with the code " +code+ " doesn't exists in the cart.");
        }
    }

    @RequestMapping(value = "/deleteCart", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCart(@RequestParam String userId) {
        try {
            cartService.deleteCart(userId);
            return ResponseEntity.ok("Cart deleted for the user " +userId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("There is no cart for the user " +userId+ " .");
        }
    }


}
