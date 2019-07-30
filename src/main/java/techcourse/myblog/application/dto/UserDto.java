package techcourse.myblog.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class UserDto {
    @NotBlank(message = "이메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "이름을 작성해주세요.")
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$", message = "2~10자리 숫자, 글자만 입력가능합니다.")
    private String name;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$", message = "8자리 이상의 글자, 숫자, 특수문자를 입력해야합니다.")
    private String password;

    public UserDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public UserDto() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return email.equals(userDto.email) &&
                name.equals(userDto.name) &&
                password.equals(userDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}
