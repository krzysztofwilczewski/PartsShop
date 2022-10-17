package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.CartItem;
import com.wilczewski.partsshopcommon.entity.Customer;

import java.util.List;

public interface ShoppingCartService{

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException;

    public List<CartItem> listCartItems(Customer customer);

    public float updateQuantity(Integer productId, Integer quantity, Customer customer);

    public void removeProduct(Integer productId, Customer customer);

    public void deleteByCustomer(Customer customer);

}
