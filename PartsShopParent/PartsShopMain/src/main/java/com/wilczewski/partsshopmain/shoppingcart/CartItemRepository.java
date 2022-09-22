package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.CartItem;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {

    public List<CartItem> findByCustomer(Customer customer);

    public CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Modifying

    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
    public void updateQuantity(Integer quantity, Integer customerId, Integer productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
    public void deleteByCustomerAndProduct(Integer customerId, Integer productId);

}
