package com.wilczewski.partsshopadmin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/users/check_email")
    public String duplicateEmail(@Param("id") Integer id, @Param("email") String email){
        return userService.isEmailUnique(id, email) ? "OK" : "DUPLIKACJA";
    }

}
