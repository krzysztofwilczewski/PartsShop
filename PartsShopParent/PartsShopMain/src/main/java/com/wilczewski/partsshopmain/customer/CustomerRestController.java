package com.wilczewski.partsshopmain.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    private CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers/check_unique_email")
    public String checkDuplicateEmail(@Param("email") String email){
        return customerService.isEmailUnique(email) ? "OK" : "DUPLIKACJA";
    }

}
