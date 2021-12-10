package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {

    private String name;
    private String nameLatin;
    private String imgUrl;
    private Integer position;
    private List<ProductEntity> products;

    public CategoryEntity() {
    }

    @Basic
    @Column(name = "name", nullable = false, unique = true, length = 25)
    public String getName() {
        return name;
    }

    public CategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Basic
    @Column
    public String getNameLatin() {
        return nameLatin;
    }

    public CategoryEntity setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
        return this;
    }

    @Lob
    public String getImgUrl() {
        return imgUrl;
    }

    public CategoryEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @Basic
    @Column(name = "position_in_shop")
    public Integer getPosition() {
        return position;
    }

    public CategoryEntity setPosition(Integer position) {
        this.position = position;
        return this;
    }

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public CategoryEntity setProducts(List<ProductEntity> products) {
        this.products = products;
        return this;
    }
}
