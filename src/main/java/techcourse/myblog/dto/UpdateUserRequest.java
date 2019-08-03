package techcourse.myblog.dto;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

public class UpdateUserRequest {

    private static final String NAME_PATTERN = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$";

    @Column(nullable = false)
    @Pattern(regexp = NAME_PATTERN)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String name, String email) {
        this.name = name;
        this.email = email;
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
}
