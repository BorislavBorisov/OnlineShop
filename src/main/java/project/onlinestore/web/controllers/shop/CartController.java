package project.onlinestore.web.controllers.shop;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.service.Item;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.CartService;
import project.onlinestore.service.ProductService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private List<Item> cart;

    private final ProductService productService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public CartController(ProductService productService, CartService cartService, ModelMapper modelMapper) {
        this.productService = productService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cart")
    public String getCart(Model model, Principal principal) {
        List<Item> cartByUsername = this.cartService.findCartByUsername(principal.getName());
        model.addAttribute("user", principal.getName());
        model.addAttribute("cart", cartByUsername);
        BigDecimal totalSum = new BigDecimal(0);
        for (Item item : cartByUsername) {
            totalSum = totalSum.add(item.getProduct().getProductPrice().multiply(BigDecimal.valueOf(item.getQty())));
        }
        model.addAttribute("totalSum", totalSum);

        return "/shop/cart";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id, Principal principal) {
        if (this.cart == null) {
            this.cart = new ArrayList<>();
            cart.add(new Item(this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class), 1));
        } else {
            int index = exists(id, this.cart);
            if (index == -1) {
                this.cart.add(new Item(this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class), 1));
            } else {
                int newQty = cart.get(index).getQty() + 1;
                this.cart.get(index).setQty(newQty);
            }
        }
        this.cartService.update(this.cart, principal.getName());
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, Principal principal) {
        ProductViewModel map = this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class);
        this.cartService.remove(map, principal.getName());
        return "redirect:/cart";
    }


    private int exists(Long id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
