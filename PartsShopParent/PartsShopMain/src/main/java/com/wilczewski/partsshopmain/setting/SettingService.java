package com.wilczewski.partsshopmain.setting;

import com.wilczewski.partsshopcommon.entity.Setting;

import java.util.List;

public interface SettingService {


    public List<Setting> getListGeneralSettings();

    public EmailSettingBag getEmailSettings();

}
