package com.wilczewski.partsshopadmin.category;

import com.wilczewski.partsshopadmin.files.FileUpload;
import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.exception.CategoryNotFoundException;
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
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listFirstPage(String sortDir, Model model){

        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, String sortDir, String keyword, Model model){
        if (sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listOfCategories = categoryService.listByPage(pageInfo, pageNumber, sortDir, keyword);

        long startCount = (pageNumber-1) * CategoryServiceImp.ROOT_CATEGORIES_PER_PAGE + 1;
        long stopCount = startCount + CategoryServiceImp.ROOT_CATEGORIES_PER_PAGE - 1;
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
        model.addAttribute("listOfCategories", listOfCategories);
        model.addAttribute("reverseSort", reverseSort);

        return "/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        Category category = new Category();
        List<Category> listCategories = categoryService.listOfCategories();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("category", category);
        model.addAttribute("title", "NOWA KATEGORIA");

        return "new_category";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);

            String uploadDir = "../category-images/" + savedCategory.getId();

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);

        } else {
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "Nowa kategoria została zapisana.");

        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name="id") Integer id, Model model, RedirectAttributes redirect){
        try{

            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listOfCategories();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("title", "Edycja kategorii (ID: " + id + ")");

            return "new_category";

        } catch(CategoryNotFoundException exception) {
            redirect.addFlashAttribute("message", exception.getMessage());

            return "redirect:/admin/categories";

        }

    }
    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){

        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "aktywna" : "nieaktywna";
        String message = "Kategoria o ID " + id + " jest " + status;
        redirectAttributes.addFlashAttribute("message",message);

        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            categoryService.delete(id);
            String categoryDir = "../category-images/" + id;
            FileUpload.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "Kategoria o ID " + id + " została usunięta");
        } catch (CategoryNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/categories";
    }

}
