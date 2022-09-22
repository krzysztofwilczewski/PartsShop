package com.wilczewski.partsshopadmin.manufacturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManufacturerRestController {

    private ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerRestController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping("/admin/manufacturers/check_unique")
    public String checkUnique(Integer id, String name, String alias){
        return manufacturerService.checkUnique(id, name, alias);
    }

}
