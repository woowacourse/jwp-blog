package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.exception.SignUpFailException;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Pattern(regexp = "^[a-zA-Z]{2,10}$")
    @Column(nullable = false)
    private String userName;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")
    @Column(nullable = false)
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
