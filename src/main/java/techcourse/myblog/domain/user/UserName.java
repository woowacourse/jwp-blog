package techcourse.myblog.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class UserName {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$");
    private static final String NAME_ERROR = "이름은 2자에서 10자 사이여야 합니다.";

    @Column(name = "name", nullable = false)
    private String userName;

    private UserName(String userName) {
        this.userName = validate(userName);
    }

    private UserName() {
    }

    public static UserName of(String userName) {
        return new UserName(userName);
    }

    private String validate(String userName) {
        Matcher matcher = NAME_PATTERN.matcher(userName);
        if (matcher.find()) {
            return userName;
        }
        throw new UserException(NAME_ERROR);
    }

    public void update(String userName) {
        this.userName = validate(userName);
    }

    public String getUserName() {
        return userName;
    }
}
