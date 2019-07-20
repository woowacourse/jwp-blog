package techcourse.myblog.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.exception.NotValidUserInfoException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String email;
    private String password;

    public User(UserDto userDto) {
        this(userDto.getUserName(), userDto.getEmail(), userDto.getPassword());
        checkConfirmPassword(userDto);
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    private void checkConfirmPassword(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new NotValidUserInfoException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
