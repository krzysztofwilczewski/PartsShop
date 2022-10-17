package com.wilczewski.partsshopmain.checkout;

import com.wilczewski.partsshopcommon.entity.*;
import com.wilczewski.partsshopmain.Utility;
import com.wilczewski.partsshopmain.address.AddressService;
import com.wilczewski.partsshopmain.customer.CustomerService;
import com.wilczewski.partsshopmain.order.OrderService;
import com.wilczewski.partsshopmain.setting.CurrencySettingBag;
import com.wilczewski.partsshopmain.setting.EmailSettingBag;
import com.wilczewski.partsshopmain.setting.SettingService;
import com.wilczewski.partsshopmain.shoppingcart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

    private CheckoutService checkoutService;
    private CustomerService customerService;
    private AddressService addressService;
    private ShoppingCartService cartService;
    private OrderService orderService;

    private SettingService settingService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, CustomerService customerService, AddressService addressService, ShoppingCartService cartService, OrderService orderService, SettingService settingService) {
        this.checkoutService = checkoutService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.settingService = settingService;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request){

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
        } else {
            model.addAttribute("shippingAddress", customer.getAddress());
        }


        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        Address defaultAddress = addressService.getDefaultAddress(customer);

        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);


        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
        cartService.deleteByCustomer(customer);

        sendOrderConfirmationEmail(request, createdOrder);

        return "order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order createdOrder) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = createdOrder.getCustomer().getEmail();
        String subject = emailSettings.getOrderConfirmationSubject();
        String content = emailSettings.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(createdOrder.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter =  new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormatter.format(createdOrder.getOrderTime());

        CurrencySettingBag currencySettings = settingService.getCurrencySettings();
        String totalAmount = Utility.formatCurrency(createdOrder.getTotal(), currencySettings);

        content = content.replace("[[name]]", createdOrder.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(createdOrder.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", createdOrder.getShippingAddress());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", createdOrder.getPaymentMethod().toString());

        helper.setText(content, true);
        mailSender.send(message);

    }

}
