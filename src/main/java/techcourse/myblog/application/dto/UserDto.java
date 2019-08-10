package techcourse.myblog.application.dto;

import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public abstract class UserDto {
    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식을 입력하세요")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$", message = "이름은 알파벳, 한글의 2~10자이어야 합니다")
    private String name;

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    Boolean matchEmail(String sessionEmail) {
        return email.equals(sessionEmail);
    }

    Boolean match(User user) {
        return email.equals(user.getEmail());
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}