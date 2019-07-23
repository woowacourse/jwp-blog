package techcourse.myblog.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MyPageEditDto {
    private static final String BLANK_NAME = "이름을 입력해주세요.";

    @Length(min = 2, max = 10)
    @NotBlank(message = BLANK_NAME)
    @Pattern(regexp = "^[a-zA-Z가-힣]+$")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
