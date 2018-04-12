package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VerificationToken {
    // 2 days until the token expire
    public static final int EXPIRATION = 2;
    public static final int VALIDATION_MAIL_TOKEN = 0;
    public static final int RECOVERY_PWD_TOKEN = 1;
    @Id
    @GeneratedValue
    private int id;
    private String token;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private int type;

    private Date expirationDate;

    public VerificationToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
