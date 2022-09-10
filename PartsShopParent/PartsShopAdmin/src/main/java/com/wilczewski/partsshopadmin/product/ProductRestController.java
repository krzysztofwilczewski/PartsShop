package com.wilczewski.partsshopadmin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/products/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name){
        return productService.checkUnique(id, name);
    }

}
