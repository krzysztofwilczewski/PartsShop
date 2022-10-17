package com.wilczewski.partsshopmain.checkout;

import com.wilczewski.partsshopcommon.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImp implements CheckoutService{

    @Override
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();
        
        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);


        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(3);
        checkoutInfo.setShippingCostTotal(12);
        float paymentTotal = productTotal + checkoutInfo.getShippingCostTotal();
        checkoutInfo.setPaymentTotal(paymentTotal);

        
        return checkoutInfo;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for (CartItem item : cartItems) {
            cost += item.getQuantity() * item.getProduct().getCost();
        }

        return cost;
    }
}
