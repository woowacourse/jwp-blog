package techcourse.myblog.service.dto;

import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static techcourse.myblog.service.utils.Messages.*;

public class UserRequestDto {

    private static final String PASSWORD_REGEXP = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}";
    private static final String NAME_REGEXP = "[a-zA-Z가-힣]{2,10}";

    @NotBlank(message = NAME_BLANK_ERROR)
    @Size(min = 2, max = 10, message = NAME_LENGTH_ERROR)
    @Pattern(regexp = NAME_REGEXP, message = NAME_FORMAT_ERROR)
    private String name;

    @NotBlank(message = EMAIL_BLANK_ERROR)
    @Email(message = EMAIL_FORMAT_ERROR)
    private String email;

    @NotBlank(message = PASSWORD_BLANK_ERROR)
    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_FORMAT_ERROR)
    private String password;

    public UserRequestDto() {
    }

    public User toEntity() {
        return new User(name, email, password);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
