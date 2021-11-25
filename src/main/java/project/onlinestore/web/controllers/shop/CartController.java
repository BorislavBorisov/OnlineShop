package project.onlinestore.web.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.onlinestore.domain.binding.ProductAddToCartCountBindingModel;
import project.onlinestore.service.CartService;

import java.security.Principal;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("users/cart/add/{productNameLatin}")
    public String addProductToCart(@PathVariable String productNameLatin,
                                   ProductAddToCartCountBindingModel productAddToCartCountBindingModel,
                                   Principal principal) {
        System.out.println();
        this.cartService.addProductToCart(productNameLatin, productAddToCartCountBindingModel.getQty(), principal.getName());
        return "redirect:/users/cart";
    }

}

