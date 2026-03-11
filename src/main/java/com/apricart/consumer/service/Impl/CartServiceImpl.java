package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Cart;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.repository.jpa.CartRepository;
import com.apricart.consumer.security.dto.request.CartRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CartService;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.service.ProductWarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductWarehouseService productWarehouseService;
    @Autowired
    ProductService productService;

    @Override
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    @Override
    public List<Cart> findByCustomerId(Customer customer) {
        LOGGER.info("Finding cart by customer id: {}", customer.getId());
        return cartRepository.findCartByCustomer(customer);
    }

    @Override
    public void addToCart(CartRequestDTO cartRequestDTO, Customer customer, LanguageType languageType) {
        LOGGER.info("Adding cart: {}", cartRequestDTO);
        Cart cart;
        cart = Cart.fromDTO(cartRequestDTO);
        cart.setCustomer(customer);
        cart.setProduct(productService.findById(cartRequestDTO.getProductId(), languageType));
        cart.setProductWarehouse(productWarehouseService.findById(cartRequestDTO.getProductWarehouseId(), languageType));
        save(cart);
    }

    @Override
    public Cart updateCart(Customer customer, Long productId, Long productWarehouseId, Integer quantity) {
        LOGGER.info("Updating cart for customer: {}, sku: {}, productWarehouseId: {}, quantity: {}", customer.getId(), productId, productWarehouseId, quantity);
        Cart cart = cartRepository.findCartByCustomerAndProductIdAndProductWarehouseId(customer, productId, productWarehouseId);
        if (cart != null) {
            cart.setQuantity(quantity.toString());
            return save(cart);
        }
        return null;
    }

    @Override
    public void removeCartItem(Customer customer, Long productId) {
        LOGGER.info("Deleting cart for customer: {}, sku: {}", customer.getId(), productId);
        cartRepository.deleteByCustomerAndProductId(customer, productId);
    }

    @Override
    public void clearCart(Customer customer) {
        LOGGER.info("Clearing cart for customer: {}", customer.getId());
        cartRepository.deleteByCustomer(customer);
    }

    @Override
    public String calculateTotal(Customer customer) {
        LOGGER.info("Calculating total for customer: {}", customer.getId());
        List<Cart> carts = findByCustomerId(customer);
        double total = 0.0;
        for (Cart item : carts) {
            if (item.getProductWarehouse().getInStock()) {
                int quantity = Integer.parseInt(item.getQuantity());
                double rate = item.getProductWarehouse().getProduct().getIsDiscounted() ? Double.parseDouble(item.getProductWarehouse().getSpecialRate()) : Double.parseDouble(item.getProductWarehouse().getCurrentRate());
                total += quantity * rate;
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        total = Double.parseDouble(df.format(total));
        return String.valueOf(total);
    }

    public Cart save(Cart cart) {
        LOGGER.info("Saving cart: {}", cart);
        return cartRepository.save(cart);
    }
}