package project.onlinestore.domain.entities;

import project.onlinestore.domain.entities.enums.PriceGroup;
import javax.persistence.*;

@Entity
@Table(name = "companies")
public class CompanyEntity extends BaseEntity {

    private String name;
    private PriceGroup priceGroup;
    private String email;
    private String address;
    private String phoneNumber;

    public CompanyEntity() {
    }

    @Basic
    @Column(name = "name", nullable = false, unique = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "price_group")
    public PriceGroup getPriceGroup() {
        return priceGroup;
    }

    public void setPriceGroup(PriceGroup priceGroup) {
        this.priceGroup = priceGroup;
    }

    @Basic
    @Column(name = "email", unique = true, length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "address", length = 121)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
