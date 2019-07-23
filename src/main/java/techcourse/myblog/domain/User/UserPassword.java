package techcourse.myblog.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPassword {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$");
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

    public boolean match(String password) {
        return this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }
}
