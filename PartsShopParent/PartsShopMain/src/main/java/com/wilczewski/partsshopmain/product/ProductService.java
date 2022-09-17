package com.wilczewski.partsshopmain.product;

import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopcommon.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {

      public Page<Product> listByCategory(int pageNumber, Integer categoryId);

      public Product getProduct(String alias) throws ProductNotFoundException;

      public Page<Product> search(String keyword, int pageNumber);

}
