package project.onlinestore.service;

import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.service.Item;
import project.onlinestore.domain.view.ProductViewModel;

import java.util.List;

public interface CartService {
    void update(List<Item> cart, String name);

    List<Item> findCartByUsername(String name);

    void remove(ProductViewModel productViewModel, String username);

    CartEntity findCartEntityByUsername(String user);

    void saveCart(CartEntity cartEntityByUsername);

    CartEntity findById(Long id);
}
