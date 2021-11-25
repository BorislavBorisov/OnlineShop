package project.onlinestore.service;

import project.onlinestore.domain.service.CartServiceModel;
import project.onlinestore.domain.view.CartViewModel;

public interface CartService {
    CartServiceModel addProductToCart(String productNameLatin, Integer qty, String name);

    CartViewModel getCartByUserName(String name);
}
