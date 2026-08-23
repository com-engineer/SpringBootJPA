package com.example.SpringBootJWT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String productName;
//    private User user;
    /*
    * [
    {
        "id": 5,
        "productName": "id 1 product 1",
        "user": {
            "email": "anikettest.com",
            "hibernateLazyInitializer": {},
            "id": 1,
            "name": "Aniket updated"
        }
    }
] in this extra ""hibernateLazyInitializer": {}," part is coming which we don't want so we need to
* use "UserDto" instead of "User - entity";
    * */
    private UserDto user;
    /*
    * [
    {
        "id": 5,
        "productName": "id 1 product 1",
        "user": {
            "id": 1,
            "name": "Aniket updated",
            "email": "anikettest.com"
        }
    }
]after change
    * */
}
