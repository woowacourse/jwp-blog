package techcourse.myblog.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateDto {
    private static final int MIN_NAME_SIZE = 2;
    private static final int MAX_NAME_SIZE = 10;
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z]+$";
    private static final String ERROR_MESSAGE_ABOUT_NAME_SIZE = "이름은 2글자 이상 10글자 이하이어야 합니다.";
    private static final String ERROR_MESSAGE_ABOUT_NAME_PATTERN = "이름은 한글 또는 영어만 입력이 가능합니다.";

    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE, message = ERROR_MESSAGE_ABOUT_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = ERROR_MESSAGE_ABOUT_NAME_PATTERN)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
