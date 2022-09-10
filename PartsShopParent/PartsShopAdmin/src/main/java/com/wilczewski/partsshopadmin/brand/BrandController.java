package com.wilczewski.partsshopadmin.brand;

import com.wilczewski.partsshopadmin.category.CategoryService;
import com.wilczewski.partsshopadmin.files.FileUpload;
import com.wilczewski.partsshopcommon.entity.Brand;
import com.wilczewski.partsshopcommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class BrandController {

    private BrandService brandService;
    private CategoryService categoryService;

    @Autowired
    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/brands")
    public String listFirstPage(Model model){
        return listByPage(1, model, "name", "asc", null);
    }

    @GetMapping("/brands/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model, @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir, @Param("keyword") String keyword){

        Page<Brand> page = brandService.listByPage(pageNumber, sortField, sortDir, keyword);
        List<Brand> listBrands = page.getContent();

        long startCount = (pageNumber-1) * BrandServiceImp.BRANDS_PER_PAGE + 1;
        long stopCount = startCount + BrandServiceImp.BRANDS_PER_PAGE - 1;
        if (stopCount > page.getTotalElements()) {
            stopCount = page.getTotalElements();
        }

        String reverseSort = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listBrands", listBrands);

        return "brands";
    }


    @GetMapping("/brands/new")
    public String newBrand(Model model){
        Brand brand = new Brand();

        List<Category> listCategories = categoryService.listOfCategories();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", brand);
        model.addAttribute("title", "NOWY PRODUCENT");

        return "new_brand";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand saveBrand = brandService.save(brand);
            String uploadDir = "../brand-images/" + saveBrand.getId();

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);

        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "Nowy producent został zapisany");
        return "redirect:/admin/brands";
    }

    @GetMapping("brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listOfCategories();

            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("title", "Edycja producenta (ID: "+id+")");

            return "new_brand";

        } catch (BrandNotFoundException exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            brandService.delete(id);
            String brandDir = "../brand-images/" + id;
            FileUpload.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message", "Producent o ID " +id+ " został usunięty");

        }catch (BrandNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/brands";
    }

}
