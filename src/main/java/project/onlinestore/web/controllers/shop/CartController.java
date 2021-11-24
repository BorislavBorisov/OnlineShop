package project.onlinestore.web.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.onlinestore.service.CartService;

import java.security.Principal;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("users/cart/add/{productNameLatin}")
    public String addProductToCart(@PathVariable String productNameLatin, Principal principal) {
        System.out.println();
        this.cartService.addProductToCart(productNameLatin, principal.getName());
        System.out.println();
        return "redirect:/users/cart";
    }
}
