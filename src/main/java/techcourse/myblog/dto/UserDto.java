package techcourse.myblog.dto;

import techcourse.myblog.domain.User;
import techcourse.myblog.validation.UserInfo;
import techcourse.myblog.validation.UserPattern;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static techcourse.myblog.validation.UserPattern.EMPTY_CONSTRAINT_MESSAGE;

public class UserDto {
    @NotBlank(message = EMPTY_CONSTRAINT_MESSAGE,
            groups = {UserInfo.class})
    @Pattern(regexp = UserPattern.NAME_PATTERN,
            message = UserPattern.NAME_CONSTRAINT_MESSAGE,
            groups = {UserInfo.class})
    private String name;

    @NotBlank(message = EMPTY_CONSTRAINT_MESSAGE)
    @Email(message = UserPattern.EMAIL_CONSTRAINT_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_CONSTRAINT_MESSAGE)
    @Pattern(regexp = UserPattern.PASSWORD_PATTERN,
            message = UserPattern.PASSWORD_CONSTRAINT_MESSAGE)
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

    public void setEmail(String email) {
        this.email = email;
    }
}
