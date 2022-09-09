package com.wilczewski.partsshopadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.wilczewski.partsshopcommon.entity"})
public class PartsShopAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartsShopAdminApplication.class, args);
    }

}
