package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.exception.SignUpFailException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String email;
    private String password;

    public User(UserSignUpRequestDto userSignUpRequestDto) {
        this(userSignUpRequestDto.getUserName(), userSignUpRequestDto.getEmail(), userSignUpRequestDto.getPassword());
        checkConfirmPassword(userSignUpRequestDto);
    }

    @Builder
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    private void checkConfirmPassword(UserSignUpRequestDto userSignUpRequestDto) {
        if (!userSignUpRequestDto.getPassword().equals(userSignUpRequestDto.getConfirmPassword())) {
            throw new SignUpFailException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void update(UserUpdateRequestDto userUpdateRequestDto) {
        this.userName = userUpdateRequestDto.getUserName();
    }
}
