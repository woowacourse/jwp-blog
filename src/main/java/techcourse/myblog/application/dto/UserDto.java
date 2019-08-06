package techcourse.myblog.application.dto;

import lombok.Getter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.validation.UserInfo;
import techcourse.myblog.domain.user.validation.UserPattern;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserDto {
    @NotBlank(message = UserPattern.NO_INPUT_MESSAGE, groups = {UserInfo.class})
    @Pattern(regexp = UserPattern.NAME,
            message = UserPattern.NAME_CONSTRAINT_MESSAGE,
            groups = {UserInfo.class})
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

    public User toUser() {
        return new User(name, email, password);
    }

//    @Override
//    public String toString() {
//        return "name: " + name
//                + "email : " + email;
//    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
