package techcourse.myblog.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9.\\-_]+@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)$");
    private static final String EMAIL_ERROR = "올바른 이메일을 입력하세요";

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public Email(String email) {
        this.email = validate(email);
    }

    public Email() {
    }

    public static Email of(String email) {
        return new Email(email);
    }

    private String validate(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.find()) {
            return email;
        }
        throw new UserException(EMAIL_ERROR);
    }

    public void update(String email) {
        this.email = validate(email);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(this.email, email.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
