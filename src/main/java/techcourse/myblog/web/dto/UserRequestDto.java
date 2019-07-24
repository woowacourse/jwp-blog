package techcourse.myblog.web.dto;

import techcourse.myblog.web.validator.UniqueEmail;

import javax.validation.constraints.*;

public class UserRequestDto {
    public static final String USERNAME_LENGTH_MESSAGE = "이름은 2글자 이상 10글자 이하여야 합니다";
    public static final String USERNAME_FORMAT_MESSAGE = "이름에는 알파벳과 공백만 사용할 수 있습니다";
    public static final String EMAIL_FORMAT_MESSAGE = "이메일 형식을 확인해주세요";
    public static final String PASSWORD_LENGTH_MESSAGE = "비밀번호는 8자 이상이어야 합니다";
    public static final String PASSWORD_FORMAT_MESSAGE = "비밀번호 형식을 확인해주세요";
    public static final String PASSWORD_EQUALITY_MESSAGE = "비밀번호가 같지 않습니다";
    public static final String EMAIL_UNIQUE_MESSAGE = "이미 존재하는 이메일입니다";

    public static final String USERNAME_PATTERN = "^[a-zA-Z ]*$";
    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    private static final int USERNAME_LENGTH_MIN = 2;
    private static final int USERNAME_LENGTH_MAX = 10;
    private static final int PASSWORD_LENGTH_MIN = 8;

    @NotBlank
    @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = USERNAME_LENGTH_MESSAGE)
    @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_FORMAT_MESSAGE)
    private String name;
    @UniqueEmail(message = EMAIL_UNIQUE_MESSAGE)
    @Email(message = EMAIL_FORMAT_MESSAGE)
    private String email;
    @NotBlank
    @Size(min = PASSWORD_LENGTH_MIN, message = PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_FORMAT_MESSAGE)
    private String password;
    private String passwordConfirm;

    public static UserRequestDto of(String userName, String email, String password, String passwordConfirm) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.name = userName;
        userRequestDto.email = email;
        userRequestDto.password = password;
        userRequestDto.passwordConfirm = passwordConfirm;
        return userRequestDto;
    }

    @AssertTrue(message = PASSWORD_EQUALITY_MESSAGE)
    public boolean isPasswordEqual() {
        return password.equals(passwordConfirm);
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
