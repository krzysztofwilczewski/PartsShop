package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.CartItem;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemTests {

    private CartItemRepository cartItemRepository;
    private TestEntityManager entityManager;

    @Autowired
    public CartItemTests(CartItemRepository cartItemRepository, TestEntityManager entityManager) {
        this.cartItemRepository = cartItemRepository;
        this.entityManager = entityManager;
    }

    @Test
    public void testSaveItem(){

        Integer customerId = 8;
        Integer productId = 1;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(1);

        CartItem savedItem = cartItemRepository.save(newItem);
        assertThat(savedItem.getId()).isGreaterThan(0);

    }

    @Test
    public void testSaveItems(){

        Integer customerId = 8;
        Integer productId = 11;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(2);

        CartItem newItem2 = new CartItem();
        newItem2.setCustomer(new Customer(customerId));
        newItem2.setProduct(new Product(2));
        newItem2.setQuantity(2);

        Iterable<CartItem> items = cartItemRepository.saveAll(List.of(newItem, newItem2));
        assertThat(items).size().isGreaterThan(0);

    }

    @Test
    public void testFindByCustomer(){
        Integer customerId = 8;

        List<CartItem> listItems = cartItemRepository.findByCustomer(new Customer(customerId));

        listItems.forEach(System.out::println);

        assertThat(listItems.size()).isEqualTo(3);
    }

}
