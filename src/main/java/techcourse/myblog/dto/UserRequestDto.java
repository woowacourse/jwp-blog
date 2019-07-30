package techcourse.myblog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class UserRequestDto {
    @NotBlank(message = "이메일을 작성해 주세요")
    @Email(message = "이메일의 양식을 지켜주세요")
    private String email;

    @NotBlank(message = "이름을 입력해 주세요")
    @Pattern(regexp = "[^ !@#$%^&*(),.?\\\":{}|<>0-9]{2,10}",
            message = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}",
            message = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
    private String password;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}",
            message = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
    private String rePassword;


    public UserRequestDto(String email, String name, String password, String rePassword) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.rePassword = rePassword;
    }

    public String getRePassword() {
        return rePassword;
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
