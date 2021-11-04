package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cities")
public class CityEntity extends BaseEntity {

    private String name;
    private String postCode;
    private List<CompanyEntity> companies;

    public CityEntity() {
    }

    @Basic
    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = " past_code", length = 10)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @OneToMany(mappedBy = "city")
    public List<CompanyEntity> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyEntity> companies) {
        this.companies = companies;
    }
}
