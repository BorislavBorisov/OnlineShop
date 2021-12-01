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

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column
    public String getNameLatin() {
        return nameLatin;
    }

    public void setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    @Lob
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Basic
    @Column(name = "position_in_shop")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
