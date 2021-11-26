package project.onlinestore.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {

    private String username;
    private String fullName;
    private String password;
    private String email;
    private String imgUrl;
    private Set<RoleEntity> authorities;
    private String firstAddress;
    private String phoneNumber;
    private String country;
    private String city;

    public UserEntity() {
    }

    @Override
    @Basic
    @Column(nullable = false, unique = true, updatable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    @Basic
    @Column(nullable = false, updatable = false, length = 50)
    public String getFullName() {
        return fullName;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    @Basic
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @Basic
    @Column(nullable = false, unique = true, length = 50)
    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    @Basic
    @Column
    public String getImgUrl() {
        return imgUrl;
    }

    public UserEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @Override
    @ManyToMany(targetEntity = RoleEntity.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    public Set<RoleEntity> getAuthorities() {
        return authorities;
    }

    public UserEntity setAuthorities(Set<RoleEntity> authorities) {
        this.authorities = authorities;
        return this;
    }

    @Basic
    @Column(length = 100)
    public String getFirstAddress() {
        return firstAddress;
    }

    public UserEntity setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
        return this;
    }

    @Basic
    @Column(length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserEntity setPhoneNumber(String postCode) {
        this.phoneNumber = postCode;
        return this;
    }

    @Basic
    @Column(length = 20)
    public String getCountry() {
        return country;
    }

    public UserEntity setCountry(String country) {
        this.country = country;
        return this;
    }

    @Basic
    @Column(length = 20)
    public String getCity() {
        return city;
    }

    public UserEntity setCity(String city) {
        this.city = city;
        return this;
    }


    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}
