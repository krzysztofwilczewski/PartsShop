package com.wilczewski.partsshopadmin.manufacturer;

import com.wilczewski.partsshopadmin.files.FileUpload;
import com.wilczewski.partsshopcommon.entity.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ManufacturerController {

    private ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping("/manufacturers")
    public String listFirstPage(String sortDir, Model model){

        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/manufacturers/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, String sortDir, String keyword, Model model){
        if (sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }

        ManufacturerPageInfo pageInfo = new ManufacturerPageInfo();
        List<Manufacturer> listOfManufacturers = manufacturerService.listByPage(pageInfo, pageNumber, sortDir, keyword);

        long startCount = (pageNumber-1) * ManufacturerServiceImp.ROOT_MANUFACTURERS_PER_PAGE + 1;
        long stopCount = startCount + ManufacturerServiceImp.ROOT_MANUFACTURERS_PER_PAGE - 1;
        if (stopCount > pageInfo.getTotalElements()) {
            stopCount = pageInfo.getTotalElements();
        }

        String reverseSort = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalpages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("listOfManufacturers", listOfManufacturers);
        model.addAttribute("reverseSort", reverseSort);

        return "/manufacturers";
    }

    @GetMapping("/manufacturers/new")
    public String newManufacturer(Model model){
        Manufacturer manufacturer = new Manufacturer();
        List<Manufacturer> listManufacturers = manufacturerService.listOfManufacturers();

        model.addAttribute("listManufacturers", listManufacturers);
        model.addAttribute("manufacturer", manufacturer);
        model.addAttribute("title", "NOWE AUTO");

        return "new_manufacturer";
    }

    @PostMapping("/manufacturers/save")
    public String saveManufacturer(Manufacturer manufacturer, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            manufacturer.setImage(fileName);

            Manufacturer savedManufacturer = manufacturerService.save(manufacturer);

            String uploadDir = "../manufacturer-images/" + savedManufacturer.getId();

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);

        } else {
            manufacturerService.save(manufacturer);
        }

        redirectAttributes.addFlashAttribute("message", "Nowy producent został zapisany.");

        return "redirect:/admin/manufacturers";

    }

    @GetMapping("/manufacturers/edit/{id}")
    public String editManufacturer(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {

            Manufacturer manufacturer = manufacturerService.get(id);
            List<Manufacturer> listManufacturers = manufacturerService.listOfManufacturers();

            model.addAttribute("manufacturer", manufacturer);
            model.addAttribute("listManufacturers", listManufacturers);
            model.addAttribute("title", "Edycja auta (ID: " + id + ")");

            return "new_manufacturer";

        } catch (ManufacturerNotFoundException exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());

            return "redirect:/admin/manufacturers";
        }
    }

    @GetMapping("/manufacturers/delete/{id}")
    public String deleteManufacturer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            manufacturerService.delete(id);
            String manufacturerDir = "../manufacturer-images/" + id;
            FileUpload.removeDir(manufacturerDir);

            redirectAttributes.addFlashAttribute("message", "Auto o ID " + id + " zostało usunięte");
        } catch (ManufacturerNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/manufacturers";
    }

}
