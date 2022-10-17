package com.wilczewski.partsshopmain.order;

import com.wilczewski.partsshopcommon.entity.*;
import com.wilczewski.partsshopmain.checkout.CheckoutInfo;

import java.util.List;

public interface OrderService {

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);

}
