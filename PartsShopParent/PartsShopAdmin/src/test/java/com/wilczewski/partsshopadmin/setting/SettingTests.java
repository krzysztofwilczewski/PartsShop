package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Setting;
import com.wilczewski.partsshopcommon.entity.SettingCategory;
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
public class SettingTests {

    private SettingRepository settingRepository;

    @Autowired
    public SettingTests(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Test
    public void testCreateSettings() {

        Setting siteLogo = new Setting("SITE_LOGO", "auto.png", SettingCategory.GENERAL);
        Setting savedSettings = settingRepository.save(siteLogo);

        assertThat(savedSettings).isNotNull();
    }

    @Test
    public void testCreateCurrencySettings() {

        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "ZŁ", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));
    }

    @Test
    public void testListSettingsByCategory() {
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }
/*
    @Test
    public void testFindByTwoCategories(){
        List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);

        settings.forEach(System.out::println);
    } */

}
