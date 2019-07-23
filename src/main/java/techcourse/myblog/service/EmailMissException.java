package techcourse.myblog.service;

public class EmailMissException extends RuntimeException {
    public EmailMissException(String msg) {
        super(msg);
    }
}
