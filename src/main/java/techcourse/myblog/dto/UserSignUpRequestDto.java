package techcourse.myblog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequestDto {
    @Pattern(regexp = "^[a-zA-Z]{2,10}$", message = "형식에 맞는 이름이 아닙니다.")
    private String userName;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "형식에 맞는 이메일이 아닙니다.")
    private String email;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "형식에 맞는 비밀번호가 아닙니다.")
    private String password;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "형식에 맞는 비밀번호가 아닙니다.")
    private String confirmPassword;

    @Builder
    public UserSignUpRequestDto(String userName, String email, String password, String confirmPassword) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
