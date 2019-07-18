package techcourse.myblog.domain;

import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;
    @Pattern(regexp = "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]{8,}$")
    private String password;
    @Pattern(regexp = "^[A-Za-zㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,10}$")
    private String name;

    public User() {
    }

    @Builder
    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
