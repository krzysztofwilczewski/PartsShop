package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryTests {

    private CountryRepository countryRepository;

    @Autowired
    public CountryTests(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Test
    public void testCreateCountry(){
        Country country = countryRepository.save(new Country("Niemcy", "DE"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCountries(){
        List<Country> listCountries = countryRepository.findAllByOrderByNameAsc();
        listCountries.forEach(System.out::println);
        assertThat(listCountries.size()).isGreaterThan(0);
    }

}
