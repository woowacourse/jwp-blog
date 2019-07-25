package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "email", length = 30, nullable = false)
    private String email;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void updateUser(UserDto userDto) {
        name = userDto.getName();
        password = userDto.getPassword();
    }
}
