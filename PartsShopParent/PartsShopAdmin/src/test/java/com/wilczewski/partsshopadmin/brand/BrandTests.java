package com.wilczewski.partsshopadmin.brand;

import com.wilczewski.partsshopcommon.entity.Brand;
import com.wilczewski.partsshopcommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandTests {

    private BrandRepository brandRepository;

    @Autowired
    public BrandTests(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Test
    public void testCreateBrad(){

        Category filters = new Category(7);

        Brand bosch = new Brand("Bosch");
        bosch.getCategories().add(filters);

        Brand save = brandRepository.save(bosch);

    }

    @Test
    public void testCreateBrand2(){

        Category rozrusznik = new Category(6);
        Category alternator = new Category(5);

        Brand hitachi = new Brand("Hitachi");
        hitachi.getCategories().add(rozrusznik);
        hitachi.getCategories().add(alternator);

        Brand save = brandRepository.save(hitachi);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll(){
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testFindById(){
        Brand brand = brandRepository.findById(1).get();
        System.out.println(brand);

        assertThat(brand.getName()).isEqualTo("Bosch");

    }

}
