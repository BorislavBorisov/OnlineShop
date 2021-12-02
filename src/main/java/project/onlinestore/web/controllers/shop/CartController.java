package project.onlinestore.web.controllers.shop;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.service.CartService;
import project.onlinestore.service.UserService;

@Controller
public class CartController {

    private final UserService userService;
    private final CartService cartService;

    public CartController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String getCart() {
        return "/shop/cart";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        Long cartId = this.userService.findCartByUsername(principal.getUsername());
        this.cartService.addProductToCart(id, principal, cartId);
        return "redirect:/cart";
    }
}
