package project.onlinestore.web.controllers.shop;

import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.view.OrderItemViewModel;
import project.onlinestore.domain.view.OrderViewModel;
import project.onlinestore.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order/{username}/{id}")
    public String orderDetails(@PathVariable Long id, @PathVariable String username, Model model) {
        Optional<OrderViewModel> order = orderService.findById(id);
        BigDecimal totalPrice = order.get().getOrderedProducts()
                .stream()
                .map(OrderItemViewModel::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
        model.addAttribute("order", order.get().getOrderedProducts());
        model.addAttribute("orderNumber", id);
        model.addAttribute("totalPrice", totalPrice);
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
