package com.wilczewski.partsshopadmin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    private CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/admin/customers/check_email")
    public String checkDuplicateEmail(Integer id, String email) {
        if (customerService.checkIsEmailUnique(id, email)) {
            return "OK";
        } else {
            return "DUPLIKACJA";
        }
    }

}
