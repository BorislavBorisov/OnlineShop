package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

    private Long id;
    private String username;
    private String series;
    private String token;
    private LocalDateTime lastUsed;

    public PersistentLogins() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Column
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column
    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}
