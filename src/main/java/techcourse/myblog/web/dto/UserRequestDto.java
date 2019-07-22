package techcourse.myblog.web.dto;

import techcourse.myblog.web.validator.UniqueEmail;

import javax.validation.constraints.*;

public class UserRequestDto {
    public static final String CONSTRAINT_VIOLATION_USERNAME_SIZE = "이름은 2글자 이상 10글자 이하여야 합니다";
    public static final String CONSTRAINT_VIOLATION_USERNAME_FORMAT = "이름에는 알파벳과 공백만 사용할 수 있습니다";
    public static final String CONSTRAINT_VIOLATION_EMAIL_FORMAT = "이메일 형식을 확인해주세요";
    public static final String CONSTRAINT_VIOLATION_PASSWORD_SIZE = "비밀번호는 8자 이상이어야 합니다";
    public static final String CONSTRAINT_VIOLATION_PASSWORD_FORMAT = "비밀번호 형식을 확인해주세요";
    public static final String CONSTRAINT_VIOLATION_PASSWORD_EQUALITY = "비밀번호가 같지 않습니다";
    public static final String CONSTRAINT_VIOLATION_EMAIL_UNIQUE = "이미 존재하는 이메일입니다";

    public static final String CONSTRAINT_USERNAME_PATTERN = "^[a-zA-Z ]*$";
    public static final String CONSTRAINT_PASSWORD_PATTERN = "((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    private static final int CONSTRAINT_USERNAME_LENGTH_MIN = 2;
    private static final int CONSTRAINT_USERNAME_LENGTH_MAX = 10;
    private static final int CONSTRAINT_PASSWORD_LENGTH_MIN = 8;

    @NotBlank
    @Size(min = CONSTRAINT_USERNAME_LENGTH_MIN, max = CONSTRAINT_USERNAME_LENGTH_MAX, message = CONSTRAINT_VIOLATION_USERNAME_SIZE)
    @Pattern(regexp = CONSTRAINT_USERNAME_PATTERN, message = CONSTRAINT_VIOLATION_USERNAME_FORMAT)
    private String name;
    @UniqueEmail(message = CONSTRAINT_VIOLATION_EMAIL_UNIQUE)
    @Email(message = CONSTRAINT_VIOLATION_EMAIL_FORMAT)
    private String email;
    @NotBlank
    @Size(min = CONSTRAINT_PASSWORD_LENGTH_MIN, message = CONSTRAINT_VIOLATION_PASSWORD_SIZE)
    @Pattern(regexp = CONSTRAINT_PASSWORD_PATTERN, message = CONSTRAINT_VIOLATION_PASSWORD_FORMAT)
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

    @AssertTrue(message = CONSTRAINT_VIOLATION_PASSWORD_EQUALITY)
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
