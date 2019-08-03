package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class UserDto {
    @NotBlank(message = "이름을 작성해 주세요.")
    @Pattern(regexp = "^[A-Za-z]{2,10}$", message = "이름은 2~10자의 영문자 이어야 합니다. ")
    private String userName;

    @NotBlank(message = "메일을 작성해 주세요.")
    @Email(message = "이메일 양식으로 적어주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "비밀번호는 8자이상의 소문자, 대문자, 숫자, 특수문자의 조합입니다.")
    private String password;
}
