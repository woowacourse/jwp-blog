package techcourse.myblog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static techcourse.myblog.domain.UserFactory.*;
import static techcourse.myblog.service.exception.UserArgumentException.*;

public class UserDto {
    @NotBlank(message = "이름을 작성해주세요.")
    @Pattern(regexp = KOREAN_ENGLISH_REGEX, message = NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = INVALID_NAME_LENGTH_MESSAGE)
    private String name;

    @NotBlank(message = "email을 작성해주세요.")
    @Email(message = "양식에 맞게 작성해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Pattern(regexp = "[(a-zA-Z0-9`~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+", message = INVALID_PASSWORD_MESSAGE)
    @Size(min = MIN_PASSWORD_LENGTH, message = INVALID_PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotBlank(message = "비밀번호를 확인해주세요.")
    private String passwordConfirm;

    public UserDto(String name, String email, String password, String passwordConfirm) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
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

    public boolean confirmPassword() {
        return password.equals(passwordConfirm);
    }
}
