package project.onlinestore.service;

import project.onlinestore.domain.entities.CartEntity;

import java.util.Map;

public interface OrderService {
    void order(CartEntity cart, String user);
}
