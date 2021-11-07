package project.onlinestore.domain.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SupplierAddBindingModel {

    private String name;
    private String email;
    private String phoneNumber;
    private String person;
    private String address;

    public SupplierAddBindingModel() {
    }

    @NotNull(message = "Полето е задължително!")
    @Size(min = 3, max = 50, message = "Името на доставчика не може да бъде по-малко от 3 и по-голямо от 50 символа!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Полето е задължително!")
    @Size(min = 3, max = 50, message = "Отговорното лице не може да бъде по-малко от 3 и по-голямо от 50 символа!")
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @NotNull(message = "Полето е задължително!")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Въведете валиден имейл адрес!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = "Полето е задължително!")
    @Size(max = 11, message = "Невалиден телефонен номер!")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Size(min = 3, max = 50, message = "Адреса на доставчика не може да бъде по-малко от 3 и по-голямо от 50 символа!")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
