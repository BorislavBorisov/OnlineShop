package project.onlinestore.web.controllers.shop;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.view.CartViewModel;
import project.onlinestore.service.CartService;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String getCart(Model model, @AuthenticationPrincipal UserDetails principal) {
        List<CartViewModel> cart = this.cartService.getCartItemsByUserUsername(principal.getUsername());
        BigDecimal totalPrice = cart
                .stream()
                .map(CartViewModel::getSumForProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", principal.getUsername());
        return "/shop/cart";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        this.cartService.addProductToCart(id, principal.getUsername());
        return "redirect:/cart";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        this.cartService.deleteProductToCart(id, principal.getUsername());
        return "redirect:/cart";
    }
}
