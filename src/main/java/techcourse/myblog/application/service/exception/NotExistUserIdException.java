package techcourse.myblog.application.service.exception;

public class NotExistUserIdException extends RuntimeException {
    private String nextView;

    public NotExistUserIdException(String message, String nextView) {
        super(message);
        this.nextView = nextView;
    }

    public String getNextView() {
        return nextView;
    }
}