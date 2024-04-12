package com.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(@RequestParam String userName) {
        try {
            return ResponseEntity.ok(userService.getUserProfile(userName));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Invalid user name " +userName+".");
        }
    }
}
