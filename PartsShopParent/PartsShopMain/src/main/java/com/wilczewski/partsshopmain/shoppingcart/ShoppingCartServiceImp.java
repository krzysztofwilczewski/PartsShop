package com.wilczewski.partsshopmain.shoppingcart;

import com.wilczewski.partsshopcommon.entity.CartItem;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.entity.Product;
import com.wilczewski.partsshopmain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShoppingCartServiceImp implements ShoppingCartService{

    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @Autowired
    public ShoppingCartServiceImp(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {

        Integer updatedQuantity = quantity;
        Product product = new Product(productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;

            if (updatedQuantity > 5) {
                throw new ShoppingCartException("Nie można dodać więcej niż " + quantity + "szt. przedmiotu, ponieważ"
                + " już dodano " + cartItem.getQuantity() + " szt. do koszyka. Maksymalna ilość szt. w koszyku to 5.");
            }
        } else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        cartItem.setQuantity(updatedQuantity);

        cartItemRepository.save(cartItem);

        return updatedQuantity;
    }

    @Override
    public List<CartItem> listCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    @Override
    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {

        cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepository.findById(productId).get();
        float subtotal = product.getDiscountPrice() * quantity;

        return subtotal;
    }

    @Override
    public void removeProduct(Integer productId, Customer customer) {
        cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
    }
}
