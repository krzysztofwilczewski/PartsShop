package com.wilczewski.partsshopmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.wilczewski.partsshopcommon.entity"})
public class PartsShopMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartsShopMainApplication.class, args);
    }

}
