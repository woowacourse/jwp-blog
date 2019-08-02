package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]{8,}$")
    private String password;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-zㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,10}$")
    private String name;

    public User() {
    }

    @Builder
    public User(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateInfo(User user) {
        this.name = user.getName();
    }

    public boolean checkAuthor(long checkingId) {
        return id == checkingId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name);
    }
}
