package techcourse.myblog.application.service.exception;

public class NotExistIdException extends RuntimeException {
    private String nextView;

    public NotExistIdException(String message, String nextView) {
        super(message);
        this.nextView = nextView;
    }

    public String getNextView() {
        return nextView;
    }
}
