package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    private BigDecimal price;
    private UserEntity customer;
    private String fullName;
    List<OrderItemEntity> orderedProducts = new ArrayList<>();

    public OrderEntity() {
    }

    @Column(nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne
    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<OrderItemEntity> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<OrderItemEntity> orderedBooks) {
        this.orderedProducts = orderedBooks;
    }

    @Column
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}


