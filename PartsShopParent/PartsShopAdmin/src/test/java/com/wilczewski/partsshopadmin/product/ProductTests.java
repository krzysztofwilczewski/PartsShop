package com.wilczewski.partsshopadmin.product;

import com.wilczewski.partsshopcommon.entity.Brand;
import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.entity.Manufacturer;
import com.wilczewski.partsshopcommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductTests {

    private ProductRepository productRepository;
    private TestEntityManager entityManager;

    @Autowired
    public ProductTests(ProductRepository productRepository, TestEntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Test
    public void testCreateNewProduct(){
        Brand brand = entityManager.find(Brand.class,2);
        Category category = entityManager.find(Category.class, 5);
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, 5);

        Product product = new Product();
        product.setName("Kierunkowskazy");
        product.setAlias("kierunkowskazy");
        product.setShortDescription("Extra kierunkowskazy Hitachi");
        product.setFullDescription("Bardzo dobre kierunkowskazy Hitachi komplet");

        product.setBrand(brand);
        product.setCategory(category);
        product.getManufacturers().add(manufacturer);

        product.setPrice(151);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);

    }

    @Test
    public void testFindAllProducts(){
        Iterable<Product> allProducts = productRepository.findAll();

        allProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id=2;
        Product product = productRepository.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();

    }

    @Test
    public void testUpdateProduct(){
        Integer id =2;
        Product product = productRepository.findById(id).get();
        product.setPrice(589);

        productRepository.save(product);

        assertThat(product.getPrice()).isEqualTo(589);


    }

    @Test
    public void testDeleteProduct(){
        Integer id = 3;
        productRepository.deleteById(id);

        Optional<Product> find = productRepository.findById(id);

        assertThat(!find.isPresent());
    }



    @Test
    public void testSaveProductWithDetails(){
        Integer productId = 1;

        Product product = productRepository.findById(productId).get();

        product.addDetail("Pojemność", "600A");
        product.addDetail("Kształt", "Sześcian");

        Product saved = productRepository.save(product);


    }

}
