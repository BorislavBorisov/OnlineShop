package project.onlinestore.web.controllers.shop;

import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.view.OrderViewModel;
import project.onlinestore.service.CartService;
import project.onlinestore.service.OrderService;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    public OrderController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/order/{username}/{id}")
    public String orderDetails(@PathVariable Long id, @PathVariable String username, Model model) {
        Optional<OrderViewModel> order = orderService.findById(id);
        model.addAttribute("order", order.get().getOrderedProducts());
        return "/shop/order";
    }

    @GetMapping("/my-orders")
    public String myOrders(@AuthenticationPrincipal UserDetails principal, Model model) {
        List<OrderViewModel> allOrdersByUser = orderService.findAllOrdersByUser(principal.getUsername());
        model.addAttribute("orders", allOrdersByUser);
        return "/shop/my-orders";
    }

    @GetMapping("/order")
    public String createOrder(@AuthenticationPrincipal UserDetails principal) throws ObjectNotFoundException {
        Long order = this.orderService.createOrder(principal.getUsername());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("redirect:/order/")
                .append(principal.getUsername())
                .append("/")
                .append(order);
        return stringBuilder.toString();
    }
}
