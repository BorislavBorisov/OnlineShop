package project.onlinestore.service.impl;

import javassist.tools.rmi.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.OrderEntity;
import project.onlinestore.domain.entities.OrderItemEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.view.CartViewModel;
import project.onlinestore.domain.view.OrderViewModel;
import project.onlinestore.repository.OrderItemRepository;
import project.onlinestore.repository.OrderRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.OrderService;
import project.onlinestore.service.UserService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository,
                            ProductRepository productRepository, UserService userService, CartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    @Override
    public Long createOrder(String username) throws ObjectNotFoundException {
        List<CartViewModel> items = cartService
                .getCartItemsByUserUsername(username);

        if (items.size() == 0) {
            throw new IllegalArgumentException("IEA");
        }

        BigDecimal totalPrice = items
                .stream()
                .map(CartViewModel::getSumForProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPrice(totalPrice);
        orderEntity.setCustomer(getUserEntity(username));

        OrderEntity newOrder = orderRepository.save(orderEntity);

        for (CartViewModel item : items) {
            OrderItemEntity orderItemEntity = getOrderItemEntity(item);
            OrderItemEntity itemEntity = orderItemEntity.setOrder(newOrder);
            orderItemRepository.save(itemEntity);
        }

        cartService.deleteOrderedProducts(username);
        return newOrder.getId();
    }

    @Transactional
    @Override
    public Optional<OrderViewModel> findById(Long id) {
        return orderRepository
                .findById(id)
                .map(this::getOrderViewModel);
    }

    @Override
    public List<OrderViewModel> findAllOrdersByUser(String username) {
        return orderRepository
                .findByCustomerUsernameOrderByIdDesc(username)
                .stream()
                .map(this::getOrderViewModel)
                .collect(Collectors.toList());
    }

    private UserEntity getUserEntity(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("IAE"));
    }

    private OrderItemEntity getOrderItemEntity(CartViewModel cartItem) throws ObjectNotFoundException {
        return new OrderItemEntity()
                .setQty(cartItem.getQty())
                .setProduct(productRepository
                        .findById(cartItem.getProduct().getId())
                        .orElseThrow(() ->
                                new ObjectNotFoundException("product")));
    }

    private OrderViewModel getOrderViewModel(OrderEntity orderEntity) {
        return modelMapper
                .map(orderEntity, OrderViewModel.class)
                .setFullName(orderEntity.getCustomer().getFullName());
    }
}
