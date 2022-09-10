package com.wilczewski.partsshopadmin.product;

import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopcommon.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductServiceImp implements ProductService{

    public static final int PRODUCTS_PER_PAGE = 5;

    public static final int PRODUCTS_ON_PAGE = 10;

    public static final int SEARCH_ON_PAGE = 10;

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Page<Product> listByPage(int pageNumber, String sortField, String sortDir, String keyword, Integer categoryId){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword !=null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepository.findAll(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {

        if (product.getId() == null){
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    public void saveProductPrice(Product productInForm){
        Product productInDB = productRepository.findById(productInForm.getId()).get();
        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        productRepository.save(productInDB);
    }

    @Override
    public String checkUnique(Integer id, String name) {

        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) return "Duplikacja";
        } else {
            if (productByName != null && productByName.getId() != id) {
                return "Duplikacja";
            }
        }

        return "OK";
    }

    @Override
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {

        Long countById = productRepository.countById(id);

        if (countById == null || countById == 0){
            throw new ProductNotFoundException("Nie można znaleźć produktu o ID " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException exception){
            throw new ProductNotFoundException("Nie można znaleźć produktu o ID " + id);
        }
    }
/*
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
 */
}
