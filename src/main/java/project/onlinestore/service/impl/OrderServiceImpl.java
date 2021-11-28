package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.OrderEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.OrderRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.OrderService;
import project.onlinestore.service.UserService;

import java.time.Instant;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserService userService, CartService cartService, ModelMapper modelMapper, OrderRepository orderRepository) {
        this.userService = userService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public void order(CartEntity cartEntity, String user) {
        OrderEntity orderEntity = new OrderEntity();
        UserEntity userEntity = this.modelMapper.map(this.userService.findUserByUsername(user), UserEntity.class);
        CartEntity byId = this.cartService.findById(cartEntity.getId());
        byId.setActive(false);

        if (this.cartService.findById(cartEntity.getId()) != null) {
            CartEntity cartEntity1 = new CartEntity();
            cartEntity1.setRegistered(Instant.now());
            cartEntity1.setUsername(user);
            for (Map.Entry<String, Integer> stringIntegerEntry : byId.getCart().entrySet()) {
                cartEntity1.getCart().put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            }
            orderEntity.setUser(user);
            cartEntity1.setActive(false);
            this.cartService.saveCart(cartEntity1);
            orderEntity.getOrder().add(cartEntity1);
            this.orderRepository.save(orderEntity);
        } else {

            orderEntity.setUser(user);
            userEntity.getCartEntity().setActive(false);
            orderEntity.getOrder().add(cartEntity);
            this.orderRepository.save(orderEntity);
            userEntity.getCartEntity().getCart().clear();
            byId.getCart().clear();
            this.userService.saveUser(userEntity);
        }

    }
}
