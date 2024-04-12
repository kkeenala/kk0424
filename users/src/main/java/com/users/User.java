package com.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String zipCode;
    private String phone;
    private String userName;
}
