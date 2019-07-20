package techcourse.myblog.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserEmail {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9.\\-_]+@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)$");
    private String email;

    public UserEmail(String email) {
        this.email = validate(email);
    }

    public static UserEmail of(String email) {
        return new UserEmail(email);
    }

    private String validate(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.find()) {
            return email;
        }
        throw new UserException();
    }

    public void update(String email) {
        this.email = validate(email);
    }

    public String getEmail() {
        return email;
    }
}
