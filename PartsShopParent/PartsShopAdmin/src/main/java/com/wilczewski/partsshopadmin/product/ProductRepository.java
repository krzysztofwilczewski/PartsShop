package com.wilczewski.partsshopadmin.product;

import com.wilczewski.partsshopcommon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Long countById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
            + "OR p.shortDescription LIKE %?1% "
            + "OR p.fullDescription LIKE %?1% "
            + "OR p.brand.name LIKE %?1% "
            + "OR p.category.name LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
            + "OR p.category.allParentIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
            + "OR p.category.allParentIDs LIKE %?2%) AND "
            + "(p.name LIKE %?3% "
            + "OR p.shortDescription LIKE %?3% "
            + "OR p.fullDescription LIKE %?3% "
            + "OR p.brand.name LIKE %?3% "
            + "OR p.category.name LIKE %?3%)")
    public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.enabled = true "
            + "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
            + " ORDER BY p.name ASC ")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

  //  public Product findByAlias(String alias);

 /*   @Query(value = "SELECT * FROM products WHERE enabled = true AND "
            + "MATCH(name, short_description, full_description) AGAINST (?1)", nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable); */

}
