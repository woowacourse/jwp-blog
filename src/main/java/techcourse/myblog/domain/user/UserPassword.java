package techcourse.myblog.domain.user;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPassword {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$");
    private static final String PASSWORD_ERROR = "대소문자, 특수문자, 숫자를 포함한 8자리 이상 문자를 입력하세요. 싫으면 시집가시고";
    private String password;

    public UserPassword(String password) {
        this.password = validate(password);
    }

    public static UserPassword of(String password) {
        return new UserPassword(password);
    }

    private String validate(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (matcher.find()) {
            return password;
        }
        throw new UserException();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
