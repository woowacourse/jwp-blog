package techcourse.myblog.dto;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

public class LoginRequest {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";

    @Column(nullable = false)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    public LoginRequest() {
    }

    public LoginRequest(@Pattern(regexp = PASSWORD_PATTERN) String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
