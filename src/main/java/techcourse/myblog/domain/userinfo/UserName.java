package techcourse.myblog.domain.userinfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.IllegalUserParamsException;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@EqualsAndHashCode(of = "name")
@Getter
@NoArgsConstructor
@Embeddable
public class UserName {

    private static final Pattern NAME_PATTERN = Pattern.compile("[가-힣ㅣa-zA-Z]{2,8}");
    private static final String ERROR_ILLEGAL_USER_NAME_MESSAGE = "이름은 2~8자의 정확한 문자로 입력해 주세요!";

    private String name;

    public UserName(String name) {
        if (isNotCorrect(name)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_NAME_MESSAGE);
        }
        this.name = name;
    }

    private boolean isNotCorrect(String name) {
        return (name != null) && !NAME_PATTERN.matcher(name).find();
    }

    public boolean match(String name) {
        return this.name.equals(name);
    }
}
