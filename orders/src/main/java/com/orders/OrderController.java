package com.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ResponseEntity<?> addItemToCart(@RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.checkout(order));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body(e.getMessage());
        }
    }

}
