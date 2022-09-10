package com.wilczewski.partsshopadmin;

import com.wilczewski.partsshopadmin.manufacturer.ManufacturerRepository;
import com.wilczewski.partsshopcommon.entity.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ManufacturerTests {

    private ManufacturerRepository manufacturerRepository;

    @Autowired
    public ManufacturerTests(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Test
    public void testCreateMainManufacturer(){
        Manufacturer manufacturer = new Manufacturer("Volvo");
        Manufacturer saved = manufacturerRepository.save(manufacturer);
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Manufacturer parent = new Manufacturer(9);
        Manufacturer subCategory = new Manufacturer("Accord", parent);
        Manufacturer subCategory1 = new Manufacturer("Civic", parent);

        manufacturerRepository.saveAll(List.of(subCategory, subCategory1));
    }

    @Test
    public void testGetManufacturer(){
        Manufacturer manufacturer = manufacturerRepository.findById(1).get();
        System.out.println(manufacturer.getName());

        Set<Manufacturer> children = manufacturer.getChildren();

        for (Manufacturer subCategory: children){
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

}
