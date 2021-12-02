package project.onlinestore.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItemEntity extends BaseEntity {

    private ProductEntity product;
    private OrderEntity order;
    private Integer qty;

    public OrderItemEntity() {
    }

    @ManyToOne
    public ProductEntity getProduct() {
        return product;
    }

    public OrderItemEntity setProduct(ProductEntity product) {
        this.product = product;
        return this;
    }

    @ManyToOne
    public OrderEntity getOrder() {
        return order;
    }

    public OrderItemEntity setOrder(OrderEntity order) {
        this.order = order;
        return this;
    }

    @Column(nullable = false)
    public Integer getQty() {
        return qty;
    }

    public OrderItemEntity setQty(Integer qty) {
        this.qty = qty;
        return this;
    }
}
