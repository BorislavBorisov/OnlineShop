package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    private List<ProductEntity> products;
    private UserEntity user;
    private BigDecimal totalSum;

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

    @OneToOne(optional = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }
}
