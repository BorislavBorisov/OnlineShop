package project.onlinestore.service;

import javassist.tools.rmi.ObjectNotFoundException;
import project.onlinestore.domain.view.OrderViewModel;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Long createOrder(String username) throws ObjectNotFoundException;

    Optional<OrderViewModel> findById(Long id);

    List<OrderViewModel> findAllOrdersByUser(String username);

    List<OrderViewModel> findAllOrders();
}
