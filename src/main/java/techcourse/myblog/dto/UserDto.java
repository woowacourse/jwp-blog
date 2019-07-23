package techcourse.myblog.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    private static final String BLANK_NAME = "이름을 입력해주세요.";
    private static final String NOT_VALID_EMAIL = "올바른 이메일 주소를 입력해주세요.";
    private static final String NOT_VALID_NAME = "제대로 된 이름을 입력해주세요.";
    private static final String NAME_LENGTH = "이름은 2자 이상 10자 이하의 길이여야 합니다.";
    private static final String PASSWORD_RULE = "패스워드는 영어 대소문자와 한글과 숫자를 모두 섞어 8글자 이상이어야 합니다.";

    @Length(min = 2, max = 10, message = NAME_LENGTH)
    @NotBlank(message = BLANK_NAME)
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = NOT_VALID_NAME)
    private String username;

    // TODO: 차후 패스워드 암호화
    @Length(min = 8, message = PASSWORD_RULE) // 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이다.
    @Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$", message = PASSWORD_RULE)
    private String password;

    @NotBlank(message = NOT_VALID_EMAIL)
    @Email(message = NOT_VALID_EMAIL)
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
