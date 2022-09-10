package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyTests {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyTests(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Test
    public void testCreateCurrencies() {

        List<Currency> listCurrencies = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("BRITISH Pound", "£", "GPB"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Polish Złoty", "ZŁ", "PLN")
        );

        currencyRepository.saveAll(listCurrencies);
        Iterable<Currency> iterable = currencyRepository.findAll();

        assertThat(iterable).size().isEqualTo(4);
    }

    @Test
    public void testAllOrderByNameAsc(){
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();
        currencies.forEach(System.out::println);

        assertThat(currencies.size()).isGreaterThan(0);
    }

}
