package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Setting;
import com.wilczewski.partsshopcommon.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImp implements SettingService{

    private SettingRepository settingRepository;

    @Autowired
    public SettingServiceImp(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    @Override
    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    @Override
    public void saveAll(Iterable<Setting> settings) {
        settingRepository.saveAll(settings);
    }

  /*  public List<Setting> getListGeneralSettings(){

        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    } */

    @Override
    public List<Setting> getMailServerSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    @Override
    public List<Setting> getMailTemplatesSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySettings() {
        return settingRepository.findByCategory(SettingCategory.CURRENCY);
    }

  /*  @Override
    public EmailSettingBag getEmailSettings() {

        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    } */

}
