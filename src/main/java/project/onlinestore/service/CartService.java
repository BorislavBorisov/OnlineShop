package project.onlinestore.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CartService {

    void addProductToCart(Long id, UserDetails principal, Long cartId);

}
