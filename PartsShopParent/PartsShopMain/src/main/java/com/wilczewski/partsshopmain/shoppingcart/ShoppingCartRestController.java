package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;
import com.wilczewski.partsshopmain.Utility;
import com.wilczewski.partsshopmain.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

    private  ShoppingCartService shoppingCartService;
    private CustomerService customerService;

    @Autowired
    public ShoppingCartRestController(ShoppingCartService shoppingCartService, CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.customerService = customerService;
    }

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
                                   HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);

            return updatedQuantity + " szt. tego produktu dodano do koszyka.";
        } catch (CustomerNotFoundException exception) {
            return "MUSISZ się zalogować aby dodać produkt do koszyka.";
        } catch (ShoppingCartException exception){
            return exception.getMessage();
        }


    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        if (email == null) {
            throw new CustomerNotFoundException("Nieautoryzowany użytkownik");
        }

        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
                                   HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);

            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException exception) {
            return "MUSISZ się zalogować aby zmieniać ilość przedmiotów w koszyku.";
        }

    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request){

        try {
         Customer  customer = getAuthenticatedCustomer(request);
            shoppingCartService.removeProduct(productId, customer);
            return "Produkt został usunięty z koszyka.";
        } catch (CustomerNotFoundException e) {
            return "MUSISZ być zalogowanym aby usunać produkt.";
        }


    }

}
