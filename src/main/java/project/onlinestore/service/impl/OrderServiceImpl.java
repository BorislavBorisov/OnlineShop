package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.OrderEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.Item;
import project.onlinestore.repository.OrderRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.OrderService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.UserService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserService userService, ProductService productService, CartService cartService, ModelMapper modelMapper, OrderRepository orderRepository) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public void order(CartEntity cartEntity, String user) {
        OrderEntity orderEntity = new OrderEntity();
        UserEntity userEntity = this.modelMapper.map(this.userService.findUserByUsername(user), UserEntity.class);
        CartEntity cartEntityByUsername = this.cartService.findCartEntityByUsername(user);

        orderEntity.setUser(user);
        orderEntity.getOrder().add(cartEntity);
        this.orderRepository.save(orderEntity);
        userEntity.getCartEntity().getCart().clear();
        cartEntityByUsername.getCart().clear();
        this.cartService.saveCart(cartEntityByUsername);
        this.userService.saveUser(userEntity);

    }
}
