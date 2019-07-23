package techcourse.myblog.domain;

public class UnFoundUserException extends RuntimeException {
    public UnFoundUserException(String message) {
        super(message);
    }
}
