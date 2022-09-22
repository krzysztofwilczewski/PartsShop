package com.wilczewski.partsshopadmin.customer;

import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String listFirstPage(Model model){
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber,Model model, String sortField,
                              String sortDir, String keyword){

        Page<Customer> page = customerService.listByPage(pageNumber, sortField, sortDir, keyword);
        List<Customer> listCustomers = page.getContent();

        long startCount = (pageNumber - 1) * CustomerServiceImp.CUSTOMERS_PER_PAGE + 1;
        long stopCount = startCount + CustomerServiceImp.CUSTOMERS_PER_PAGE - 1;
        if (stopCount > page.getTotalElements()){
            stopCount = page.getTotalElements();
        }


        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSort", sortDir.equals("asc") ? "desc" : "asc");

        return "customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){
        customerService.updateCustomerEnabledStatus(id, enabled);
        String status = enabled ? "aktywny" : "nieaktywny";
        String message = "Użytkownik o ID " + id + " jest " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);

            return "customer_detail";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/customers";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Customer customer = customerService.get(id);
            List<Country> countries = customerService.listAllCountries();

            model.addAttribute("customer", customer);
            model.addAttribute("listCountries", countries);
            model.addAttribute("pageTitle", String.format("Edycja klienta (ID: %d)", id));

            return "customer";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes){
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "Dane użytkownika o ID " + customer.getId() + " zostały zaktualizowane.");
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Użytkownik o ID " + id + " został usunięty!");
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/admin/customers";
    }

}
