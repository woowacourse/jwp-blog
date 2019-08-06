package techcourse.myblog.exception;

public class EmailRepetitionException extends RuntimeException {
    public EmailRepetitionException(String message) {
        super(message);
    }
}
