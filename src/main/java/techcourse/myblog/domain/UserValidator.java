package techcourse.myblog.domain;

import java.util.regex.Pattern;

public class UserValidator {
    static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
    public static final String NAME_REGEX = "[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]{2,10}";

    public static final String EMAIL_NOT_MATCH_MESSAGE = "메일의 양식을 지켜주세요.";
    public static final String NAME_NOT_MATCH_MESSAGE = "이름은 2자이상 10자이하이며, 숫자나 특수문자가 포함될 수 없습니다.";
    public static final String PASSWORD_NOT_MATCH_MESSAGE = "비밀번호는 8자 이상의 소문자,대문자,숫자,특수문자의 조합이여야 합니다.";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public static void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).find()) {
            throw new IllegalArgumentException(EMAIL_NOT_MATCH_MESSAGE);
        }
    }

    public static void validatePassword(final String password) {
        if (!PASSWORD_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException(PASSWORD_NOT_MATCH_MESSAGE);
        }
    }

    public static void validateName(final String name) {
        if (!NAME_PATTERN.matcher(name).find()) {
            throw new IllegalArgumentException(NAME_NOT_MATCH_MESSAGE);
        }
    }

}
