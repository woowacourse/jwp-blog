package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,
            unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean matchPassword(String password) {
        if (this.password == null) {
            throw new IllegalUserException("password 값이 설정되어 있지 않습니다.");
        }
        return this.password.equals(password);
    }

    public boolean matchEmail(String email) {
        if (this.email == null) {
            throw new IllegalUserException("password 값이 설정되어 있지 않습니다.");
        }
        return this.email.equals(email);
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
