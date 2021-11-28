package project.onlinestore.web.controllers.shop;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.Item;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.CartService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public CartController(ProductService productService, UserService userService, CartService cartService, ModelMapper modelMapper) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cart")
    public String getCart(Model model, Principal principal) {
        List<Item> cartByUsername = this.cartService.findCartByUsername(principal.getName());
        model.addAttribute("cart", cartByUsername);
        if (cartByUsername.size() == 0) {
            return "/shop/cart";
        }

        BigDecimal totalSum = new BigDecimal(0);
        for (Item item : cartByUsername) {
            totalSum = totalSum.add(item.getProduct().getProductPrice().multiply(BigDecimal.valueOf(item.getQty())));
        }
        model.addAttribute("totalSum", totalSum);

        return "/shop/cart";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id, Principal principal) {
        UserEntity user = this.modelMapper.map(this.userService.findUserByUsername(principal.getName()), UserEntity.class);

        CartEntity cartEntity = user.getCartEntity();

        if (cartEntity == null) {
            List<Item> newCart = new ArrayList<>();
            newCart.add(new Item(this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class), 1));
            this.cartService.update(newCart, principal.getName());
        } else {
            Map<String, Integer> userCart = user.getCartEntity().getCart();
            List<Item> newCart = new ArrayList<>();
            for (Map.Entry<String, Integer> s : userCart.entrySet()) {
                ProductEntity productByName = this.productService.findProductByName(s.getKey());
                newCart.add(new Item(this.modelMapper.map(productByName, ProductViewModel.class), s.getValue()));
            }
            int index = exists(id, newCart);
            if (index == -1) {
                newCart.add(new Item(this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class), 1));
            } else {
                int newQty = newCart.get(index).getQty() + 1;
                newCart.get(index).setQty(newQty);
            }
            this.cartService.update(newCart, principal.getName());
        }
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
