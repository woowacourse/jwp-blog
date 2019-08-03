package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$")
    @Column(nullable = false)
    @Size(min = 2, max = 10)
    private String name;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,}$")
    @Column(nullable = false)
    @Size(max = 30)
    private String password;
    @Email
    @Column(nullable = false, unique = true)
    @Size(max = 20)
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public boolean isNotMatch(User target) {
        return id != target.getId();
    }

    public boolean isNotMatch(UserDto target) {
        return id != target.getId();
    }

    public void update(UserDto userDto) {
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
    }

    public void checkMatch(UserDto userDto) {
        if (isNotMatch(userDto)) {
            throw new IllegalArgumentException();
        }
    }
}

