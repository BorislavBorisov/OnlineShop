package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    private String username;
    private Map<String, Integer> cart;
    private Boolean isActive;

    public CartEntity() {
        this.cart = new HashMap<>();
    }

    @Basic
    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<String, Integer> cart) {
        this.cart = cart;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
