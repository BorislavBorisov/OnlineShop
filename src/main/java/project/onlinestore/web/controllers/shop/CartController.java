package project.onlinestore.web.controllers.shop;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.service.Item;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.ProductService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private List<Item> cart;

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public CartController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/cart")
    public String getCart(Model model) {
        if (this.cart != null) {
            model.addAttribute("cart", this.cart);

            BigDecimal sum = new BigDecimal(0);
            for (Item item : cart) {
                sum = sum.add(item.getTotalSum());
            }
            model.addAttribute("totalSum", sum);
        }

        return "/shop/cart";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id) {
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
