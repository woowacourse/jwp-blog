package techcourse.myblog.service.dto;

import techcourse.myblog.support.validation.UserInfo;
import techcourse.myblog.domain.User;
import techcourse.myblog.support.validation.pattern.UserPattern;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    @NotBlank(message = UserPattern.NO_INPUT_MESSAGE)
    @Pattern(regexp = UserPattern.NAME,
            message = UserPattern.NAME_CONSTRAINT_MESSAGE,
            groups={UserInfo.class})
    private String name;
    
    @NotBlank(message = UserPattern.NO_INPUT_MESSAGE)
    @Pattern(regexp = UserPattern.EMAIL,
            message = UserPattern.EMAIL_CONSTRAINT_MESSAGE)
    private String email;
    
    @NotBlank(message = UserPattern.NO_INPUT_MESSAGE)
    @Pattern(regexp = UserPattern.PASSWORD,
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
