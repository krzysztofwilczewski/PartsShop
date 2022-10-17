package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Setting;

import java.util.List;

public interface SettingService {

    public List<Setting> listAllSettings();

    public GeneralSettingBag getGeneralSettings();

    public void saveAll(Iterable<Setting> settings);

    public List<Setting> getMailServerSettings();

    public List<Setting> getMailTemplatesSettings();

    public List<Setting> getCurrencySettings();

    public List<Setting> getPaymentSettings();


}
