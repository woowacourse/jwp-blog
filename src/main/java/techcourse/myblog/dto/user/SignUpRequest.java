package techcourse.myblog.dto.user;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

public class SignUpRequest {

    private static final String NAME_PATTERN = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";

    @Column(nullable = false)
    @Pattern(regexp = NAME_PATTERN)
    private String name;
    @Column(nullable = false)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;

    public SignUpRequest() {
    }

    public SignUpRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
