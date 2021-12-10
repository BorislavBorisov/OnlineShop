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
    private String person;
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

    public SupplierEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Basic
    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public SupplierEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    @Basic
    @Column(name = "phone_number", nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SupplierEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Basic
    @Column(name = "address", nullable = false)
    public String getAddress() {
        return address;
    }

    public SupplierEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    @Basic
    @Column(name = "person", nullable = false)
    public String getPerson() {
        return person;
    }

    public SupplierEntity setPerson(String person) {
        this.person = person;
        return this;
    }

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.REMOVE)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public SupplierEntity setProducts(List<ProductEntity> products) {
        this.products = products;
        return this;
    }
}
