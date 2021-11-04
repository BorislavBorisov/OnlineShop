package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class SupplierEntity extends BaseEntity {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<ProductEntity> products;
    // Todo
    // Add delivery entity for documents

    public SupplierEntity() {
    }

    @Basic
    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToMany(mappedBy = "supplier")
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
