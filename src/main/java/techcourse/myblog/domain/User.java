package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import techcourse.myblog.controller.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Size(min = 2, max = 10)
    private String name;
    @Column(nullable = false)
    @Size(max = 30)
    private String password;
    @Column(nullable = false, unique = true)
    @Size(max = 20)
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void matchPassword(UserDto userDto) {
        if (userDto.getPassword().equals(password)) {
            return;
        }
        throw new IllegalArgumentException();
    }
}

