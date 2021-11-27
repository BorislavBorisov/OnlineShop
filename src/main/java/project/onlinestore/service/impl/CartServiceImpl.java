package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.Item;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.CartRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(UserService userService, CartRepository cartRepository, ProductService productService, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void update(List<Item> cart, String username) {
        UserEntity user = this.modelMapper.map(this.userService.findUserByUsername(username), UserEntity.class);
        if (user.getCartEntity() == null) {
            CartEntity cartEntity = new CartEntity();
            for (Item item : cart) {
                if (!cartEntity.getCart().containsKey(item.getProduct().getProductName())) {
                    cartEntity.getCart().put(item.getProduct().getProductName(), item.getQty());

                } else {
                    for (Map.Entry<String, Integer> p : cartEntity.getCart().entrySet()) {
                        if (item.getProduct().getProductName().equals(p.getKey())) {
                            p.setValue(p.getValue() + item.getQty());
                        }
                    }
                }
            }
            cartEntity.setActive(true);
            cartEntity.setUsername(user.getUsername());
            cartEntity.setRegistered(Instant.now());
            cartEntity.setModified(Instant.now());
            this.cartRepository.save(cartEntity);
            user.setCartEntity(cartEntity);
            this.userService.saveUser(user);

        } else {
            CartEntity cartEntity = user.getCartEntity();
            for (Item item : cart) {
                if (!cartEntity.getCart().containsKey(item.getProduct().getProductName())) {
                    cartEntity.getCart().put(item.getProduct().getProductName(), item.getQty());

                } else {
                    for (Map.Entry<String, Integer> p : cartEntity.getCart().entrySet()) {
                        if (item.getProduct().getProductName().equals(p.getKey())) {
                            p.setValue(p.getValue() + item.getQty());
                        }
                    }
                }
                this.cartRepository.save(cartEntity);
            }
        }

    }

    @Override
    public List<Item> findCartByUsername(String name) {
        UserServiceModel userByUsername = this.userService.findUserByUsername(name);
        Map<String, Integer> cart = userByUsername.getCartEntity().getCart();

        List<Item> out = new ArrayList<>();

        for (Map.Entry<String, Integer> s : cart.entrySet()) {
            String key = s.getKey();
            ProductEntity productEntity = this.productRepository.findByProductName(key)
                    .orElse(null);

            out.add(new Item(this.modelMapper.map(productEntity, ProductViewModel.class), s.getValue()));
        }
        return out;
    }

    @Override
    public void remove(ProductViewModel productViewModel, String username) {
        UserEntity user = this.modelMapper.map(this.userService.findUserByUsername(username), UserEntity.class);
        CartEntity cartEntity = user.getCartEntity();

    }
}
