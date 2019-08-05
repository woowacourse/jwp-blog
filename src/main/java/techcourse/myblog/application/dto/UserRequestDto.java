package techcourse.myblog.application.dto;

import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRequestDto extends UserDto {
    @NotBlank(message = "비밀번호을 입력하세요")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$", message = "비밀번호는 8자 이상의 알파벳, 숫자, 특수문자로 이루어져야 합니다")
    private String password;

    public UserRequestDto(String email, String name, String password) {
        super(email, name);
        this.password = password;
    }

    public Boolean match(User user) {
        return super.match(user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}