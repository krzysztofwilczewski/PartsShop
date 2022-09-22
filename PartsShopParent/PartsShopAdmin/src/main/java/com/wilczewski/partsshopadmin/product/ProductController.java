package com.wilczewski.partsshopadmin.product;

import com.wilczewski.partsshopadmin.brand.BrandService;
import com.wilczewski.partsshopadmin.category.CategoryService;
import com.wilczewski.partsshopadmin.configuration.ShopUserDetails;
import com.wilczewski.partsshopadmin.files.FileUpload;
import com.wilczewski.partsshopadmin.manufacturer.ManufacturerService;
import com.wilczewski.partsshopcommon.entity.Brand;
import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.entity.Manufacturer;
import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopcommon.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private ProductService productService;
    private BrandService brandService;
    private ManufacturerService manufacturerService;

    private CategoryService categoryService;


    @Autowired
    public ProductController(ProductService productService, BrandService brandService, ManufacturerService manufacturerService, CategoryService categoryService) {
        this.productService = productService;
        this.brandService = brandService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String listFirstPage(Model model){

        return listByPage(1, model, "name", "asc", null, 0);
    }

    @GetMapping("/products/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model, String sortField,
                              String sortDir, String keyword, Integer categoryId){

        Page<Product> page = productService.listByPage(pageNumber, sortField, sortDir, keyword, categoryId);
        List<Product> listProducts = page.getContent();

        List<Category> listCategories = categoryService.listOfCategories();

        long startCount = (pageNumber-1) * ProductServiceImp.PRODUCTS_PER_PAGE + 1;
        long stopCount = startCount + ProductServiceImp.PRODUCTS_PER_PAGE - 1;
        if (stopCount > page.getTotalElements()) {
            stopCount = page.getTotalElements();
        }

        String reverseSort = sortDir.equals("asc") ? "desc" : "asc";

        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);


        return "products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        List<Brand> listBrands = brandService.listAll();
        List<Manufacturer> listManufacturers = manufacturerService.listOfManufacturers();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listManufacturers", listManufacturers);
        model.addAttribute("title", "NOWY PRODUKT");
        model.addAttribute("numberOfExistingExtraImages",0);


        return "new_product";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal ShopUserDetails loggedUser) throws IOException {

        if (loggedUser.hasRole("Seller")) {
            productService.saveProductPrice(product);

            redirectAttributes.addFlashAttribute("message", "Produkt został zapisany");

            return "redirect:/admin/products";
        }

        ProductHelper.setMainImageName(mainImageMultipart, product);
        ProductHelper.setExistingImageNames(imageIDs, imageNames, product);
        ProductHelper.setNewExtraImageNames(extraImageMultiparts, product);
        ProductHelper.setProductDetails(detailIDs, detailNames, detailValues, product);


        Product savedProduct = productService.save(product);

        ProductHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        ProductHelper.deleteExtraImagesWeredRemovedOnForm(product);


        redirectAttributes.addFlashAttribute("message", "Produkt został zapisany");

        return "redirect:/admin/products";
    }



    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){

        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "aktywny" : "nieaktywny" ;
        String message = "Produkt o ID " + id + " jest " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            productService.delete(id);
            String productExtraImageDir = "../product-images/" + id + "/extras";
            String productImageDir = "../product-images/" + id;

            FileUpload.removeDir(productExtraImageDir);
            FileUpload.removeDir(productImageDir);

            redirectAttributes.addFlashAttribute("message", "Produkt o ID " +id+ " został usunięty.");
        } catch (ProductNotFoundException exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            List<Manufacturer> listManufacturers = manufacturerService.listOfManufacturers();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("listManufacturers", listManufacturers);
            model.addAttribute("title", "Edycja produktu (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);



            return "new_product";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/products";
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            List<Manufacturer> listManufacturers = manufacturerService.listOfManufacturers();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);


            return "product_detail_modal";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/products";
        }
    }

}
