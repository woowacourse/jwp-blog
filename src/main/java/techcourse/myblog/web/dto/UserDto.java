package techcourse.myblog.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class UserDto {
    private static final int MIN_USER_NAME_SIZE = 2;
    private static final int MAX_USER_NAME_SIZE = 10;
    private static final String USER_NAME_PATTERN = "^[a-zA-Zㄱ-ㅎ가-힣]*$";
    private static final String USER_PASSWORD_PATTERN = "(?=^.{8,}$)((?=.*\\\\d)(?=.*\\\\W+))(?![.\\\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    @Min(value = MIN_USER_NAME_SIZE)
    @Max(value = MAX_USER_NAME_SIZE)
    @Pattern(regexp = USER_NAME_PATTERN)
    private String name;

    @Email
    private String email;

    @Pattern(regexp = USER_PASSWORD_PATTERN)
    private String password;
    private String passwordConfirm;

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
