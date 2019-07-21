package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String email;
    private String name;
    private String password;

    public User() {
    }

    public User(final String email, final String name, final String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void update(final String name) {
        this.name = name;
    }
}
