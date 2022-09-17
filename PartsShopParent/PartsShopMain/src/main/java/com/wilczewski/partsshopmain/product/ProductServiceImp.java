package com.wilczewski.partsshopmain.product;

import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopcommon.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImp implements ProductService{

    public static final int PRODUCTS_ON_PAGE = 10;

    public static final int SEARCH_ON_PAGE = 10;


    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Page<Product> listByCategory(int pageNumber, Integer categoryId) {
        String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNumber - 1, PRODUCTS_ON_PAGE);

        return productRepository.listByCategory(categoryId, categoryIDMatch, pageable);
    }

    @Override
    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Nie znaleziono produktu " + alias);
        }
        return product;
    }

    @Override
    public Page<Product> search(String keyword, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber - 1, SEARCH_ON_PAGE);
        return productRepository.search(keyword, pageable);
    }


}
