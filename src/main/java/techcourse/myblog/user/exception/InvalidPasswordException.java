package techcourse.myblog.user.exception;

public class InvalidPasswordException extends SignUpException {
    public InvalidPasswordException() {
        super("패스워드 : 8자 이상의 대문자, 소문자, 특수문자, 숫자 각각 1개 이상 포함");
    }
}
