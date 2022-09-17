package com.wilczewski.partsshopmain.product;

import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopcommon.exception.CategoryNotFoundException;
import com.wilczewski.partsshopcommon.exception.ProductNotFoundException;
import com.wilczewski.partsshopmain.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    private CategoryService categoryService;
    private ProductService productService;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model){
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNumber}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNumber") int pageNumber, Model model){

        try {
            Category category = categoryService.getCategory(alias);

            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            Page<Product> pageProducts = productService.listByCategory(pageNumber, category.getId());
            List<Product> listProducts = pageProducts.getContent();

            long startCount = (pageNumber - 1) * ProductServiceImp.PRODUCTS_ON_PAGE + 1;
            long stopCount = startCount + ProductServiceImp.PRODUCTS_ON_PAGE - 1;
            if (stopCount > pageProducts.getTotalElements()) {
                stopCount = pageProducts.getTotalElements();
            }


            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("startCount", startCount);
            model.addAttribute("stopCount", stopCount);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("category", category);

            return "products_by_category";
        } catch (CategoryNotFoundException ex) {
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model){

        try {
            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());


            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getShortName());

            return "product_detail";
        } catch (ProductNotFoundException e){
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model){
        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchByPage(@Param("keyword") String keyword, @PathVariable("pageNumber") int pageNumber, Model model){

        Page<Product> pageProducts = productService.search(keyword, pageNumber);
        List<Product> listResult = pageProducts.getContent();

        long startCount = (pageNumber - 1) * ProductServiceImp.SEARCH_ON_PAGE + 1;
        long stopCount = startCount + ProductServiceImp.SEARCH_ON_PAGE - 1;
        if (stopCount > pageProducts.getTotalElements()) {
            stopCount = pageProducts.getTotalElements();
        }


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Wyniki wyszukiwania");

        model.addAttribute("keyword", keyword);
        model.addAttribute("listResult", listResult);

        return "search";
    }

}
