package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    private List<ProductEntity> products;
    private Integer count;
    private BigDecimal totalPrice;
    private UserEntity user;
    private Boolean isActive;

    public CartEntity() {
        this.products = new ArrayList<>();
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    @Basic
    @Column
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Basic
    @Column
    public Boolean getDone() {
        return isActive;
    }

    public void setDone(Boolean done) {
        isActive = done;
    }
}