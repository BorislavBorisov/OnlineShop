package project.onlinestore.web.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.service.OrderService;
import project.onlinestore.service.UserService;

@Controller
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/order/{user}")
    public String order(@PathVariable String user){
        UserServiceModel userByUsername = this.userService.findUserByUsername(user);
        CartEntity cartEntity = userByUsername.getCartEntity();
        this.orderService.order(cartEntity, user);
        return "/shop/order";
    }
}
