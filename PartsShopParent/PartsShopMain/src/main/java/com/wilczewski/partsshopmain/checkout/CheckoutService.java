package com.wilczewski.partsshopmain.checkout;

import com.wilczewski.partsshopcommon.entity.CartItem;

import java.util.List;

public interface CheckoutService {

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems);

}
