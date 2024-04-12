package com.users;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {

    // this data will be in DB. For assessment purposes I'm using prefilled bean objects on application start.
 User k = new User(1, "Kalyan", "Keenala",
         "555 NE 8th St, Ft Lauderdale FL", " address2", "33304", "6694997077", "kkeenala");

 User r = new User(1, "Ravi", "Kumar",
            "8444 E Indian School Rd, Scottsdale, AZ ", " address2", "85251", "6690010010", "rkumar");

    List<User> users;

    {
        users = new ArrayList<>();
        users.add(k);
        users.add(r);
    }


    public User getUserProfile(String userName) throws Exception {

        Optional<User> user =  users.stream().filter(u -> u.getUserName().equals(userName)).findFirst();

        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new Exception();
        }

    }





}
