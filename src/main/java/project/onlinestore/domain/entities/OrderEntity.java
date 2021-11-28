package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    private String user;
    private List<CartEntity> order;

    public OrderEntity() {
        this.order = new ArrayList<>();
    }

    @Basic
    @Column(name = "username")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @OneToMany
    public List<CartEntity> getOrder() {
        return order;
    }

    public void setOrder(List<CartEntity> order) {
        this.order = order;
    }
}


