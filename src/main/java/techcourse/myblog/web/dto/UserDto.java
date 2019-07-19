package techcourse.myblog.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class UserDto {
    @Min(value = 2)
    @Max(value = 10)
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣]*$")
    private String name;

    @Email()
    private String email;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\\\d)(?=.*\\\\W+))(?![.\\\\n])(?=.*[A-Z])(?=.*[a-z]).*$")
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
