package techcourse.myblog.exception;

public class InvalidNameException extends SignUpException {
    public InvalidNameException() {
        super("이름 : 2 ~ 10자의 한글 또는 영어(숫자, 특수문자 사용 불가)");
    }
}
