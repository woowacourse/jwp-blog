package techcourse.myblog.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class UserRequest {
    @NotBlank(message = "이메일을 작성해 주세요.")
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해 주세요.")
    @Pattern(regexp = "[^ !@#$%^&*(),.?\\\":{}|<>0-9]{2,10}",
            message = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.")
    private String name;

    @NotBlank(message = "비밀먼호를 입력해 주세요.")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}",
            message = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
    private String password;

    public UserRequest(final String email, final String name, final String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);

        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
