package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopadmin.files.FileUpload;
import com.wilczewski.partsshopcommon.entity.Currency;
import com.wilczewski.partsshopcommon.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class SettingController {

    private SettingService settingService;
    private CurrencyRepository currencyRepository;

    @Autowired
    public SettingController(SettingService settingService, CurrencyRepository currencyRepository) {
        this.settingService = settingService;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/settings")
    public String listAll(Model model) {

        List<Setting> listSettings = settingService.listAllSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

        GeneralSettingBag settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingsValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "Główne ustawienia zostały zapisane.");

        return "redirect:/admin/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;

            settingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo/";
            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest servletRequest, GeneralSettingBag settingBag){

        Integer currencyId = Integer.parseInt(servletRequest.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }

    }

    private void updateSettingsValuesFromForm(HttpServletRequest request, List<Setting> listSettings){
        for (Setting setting : listSettings){
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateSettingsValuesFromForm(request, mailServerSettings);

        redirectAttributes.addFlashAttribute("message", "Ustawienia Email zostały zapisane");

        return "redirect:/admin/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailTemplateSettings = settingService.getMailTemplatesSettings();
        updateSettingsValuesFromForm(request, mailTemplateSettings);

        redirectAttributes.addFlashAttribute("message", "Ustawienia Email zostały zapisane");

        return "redirect:/admin/settings";
    }

}
