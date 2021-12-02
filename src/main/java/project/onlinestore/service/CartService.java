package project.onlinestore.service;

import project.onlinestore.domain.view.CartViewModel;

import java.util.List;

public interface CartService {

    void addProductToCart(Long id, String username);

    List<CartViewModel> getCartItemsByUserUsername(String username);

    void deleteProductToCart(Long id, String username);

    void deleteOrderedProducts(String username);
}
