package techcourse.myblog.exception;

public class EmailRepetitionException extends RuntimeException {
    public EmailRepetitionException(String s) {
        super(s);
    }
}
