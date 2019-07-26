package techcourse.myblog.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginDto {
    @NotBlank(message = "이메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$", message = "8자리 이상의 글자, 숫자, 특수문자를 입력해야합니다.")
    private String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginDto of(UserDto userDto) {
        return new LoginDto(userDto.getEmail(), userDto.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}