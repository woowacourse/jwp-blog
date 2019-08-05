package techcourse.myblog.web.dto;

import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @Size(min = 2, max = 10)
    @NotBlank
    @Pattern(regexp = "^(?!.*[~`!@#$%\\^&*()-])(?!.*\\d).{2,10}$", message = "이름 사이즈는 2~10 크기 입니다.")
    private String name;

    @Email
    private String email;

    // TODO: 2019-07-18 MESSAGE!!
    @Size(min = 8, max = 14)
    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,14}$",
            message = "숫자, 특수문자와 대소문자를 포함한 8~14 크기입니다.")
    private String password;

    public UserDto(@Size(min = 2, max = 10) @NotBlank @Pattern(regexp = "^(?!.*[~`!@#$%\\^&*()-])(?!.*\\d).{2,10}$", message = "이름 사이즈는 2~10 크기 입니다.") String name, @Email String email, @Size(min = 8, max = 14) @NotBlank @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,14}$",
            message = "숫자, 특수문자와 대소문자를 포함한 8~14 크기입니다.") String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public User create() {
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
