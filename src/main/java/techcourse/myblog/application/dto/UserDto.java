package techcourse.myblog.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    private Long id;

    @NotBlank(message = "이메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "이름을 작성해주세요.")
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$", message = "2~10자리 숫자, 글자만 입력가능합니다.")
    private String name;

    @NotBlank(message = "비밀번호를 작성해주세요.")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$", message = "8자리 이상의 글자, 숫자, 특수문자를 입력해야합니다.")
    private String password;

    public UserDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean compareEmail(String email) {
        return email.equals(this.email);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}