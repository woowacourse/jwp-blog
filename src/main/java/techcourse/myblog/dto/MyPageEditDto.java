package techcourse.myblog.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MyPageEditDto {
    private static final String BLANK_NAME = "이름을 입력해주세요.";
    private static final String NAME_LENGTH = "이름은 2자 이상 10자 이하의 길이여야 합니다.";
    private static final String NOT_VALID_NAME = "제대로 된 이름을 입력해주세요.";

    @Length(min = 2, max = 10, message = NAME_LENGTH)
    @NotBlank(message = BLANK_NAME)
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = NOT_VALID_NAME)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
