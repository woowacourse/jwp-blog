package techcourse.myblog.dto;

import techcourse.myblog.UserInfo;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    @NotBlank(message = "이름을 입력해주세요",
            groups={UserInfo.class})
    @Pattern(regexp = "[^ !@#$%^&*(),.?\\\":{}|<>0-9]{2,10}",
            message = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.",
            groups={UserInfo.class})
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}",
            message = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
    private String password;

    public UserDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toUser() {
        return new User(name, email, password);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
