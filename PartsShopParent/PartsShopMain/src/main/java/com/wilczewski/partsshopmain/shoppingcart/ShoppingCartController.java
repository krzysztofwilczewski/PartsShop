package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.CartItem;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;
import com.wilczewski.partsshopmain.Utility;
import com.wilczewski.partsshopmain.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    private CustomerService customerService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.customerService = customerService;
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);

        float estimatedTotal = 0.0F;

        for (CartItem item : cartItems){
            estimatedTotal += item.getSubtotal();
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }

}
