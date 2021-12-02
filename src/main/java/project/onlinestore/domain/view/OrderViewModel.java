package project.onlinestore.domain.view;

import java.math.BigDecimal;
import java.util.List;

public class OrderViewModel {

    private Long id;
    private BigDecimal price;
    private String fullName;
    private List<OrderItemViewModel> orderedProducts;

    public OrderViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<OrderItemViewModel> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<OrderItemViewModel> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public String username() {
        return fullName;
    }

    public OrderViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}
