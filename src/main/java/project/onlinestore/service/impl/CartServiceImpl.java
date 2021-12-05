package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartItemEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.view.CartViewModel;
import project.onlinestore.repository.CartItemRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.CartService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addProductToCart(Long id, String username) {
        Optional<CartItemEntity> cartItemEntity = cartItemRepository
                .findByProductIdAndUserUsername(id, username);

        if (cartItemEntity.isPresent()) {
            CartItemEntity cartItem = cartItemEntity.get();
            cartItem.setQty(cartItem.getQty() + 1);
            this.cartItemRepository.save(cartItem);
        } else {
            ProductEntity product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("IAE"));

            UserEntity user = this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("IAE"));

            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setProduct(product);
            cartItem.setQty(1);
            cartItem.setUser(user);
            cartItem.setRegistered(Instant.now());
            cartItem.setModified(Instant.now());
            this.cartItemRepository.save(cartItem);
        }

    }

    @Override
    public List<CartViewModel> getCartItemsByUserUsername(String username) {
        return this.cartItemRepository
                .findAllByUserUsername(username).stream()
                .map(cartItemEntity -> modelMapper
                        .map(cartItemEntity, CartViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductToCart(Long id, String username) {
        CartItemEntity cartItemEntity = cartItemRepository
                .findByProductIdAndUserUsername(id, username)
                .orElseThrow(() -> new IllegalArgumentException("IAE"));

        this.cartItemRepository.deleteById(cartItemEntity.getId());
    }

    @Override
    public void deleteOrderedProducts(String username) {
        cartItemRepository
                .deleteAllByUserUsername(username);
    }


}
