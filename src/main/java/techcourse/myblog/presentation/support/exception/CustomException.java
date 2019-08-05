package techcourse.myblog.presentation.support.exception;

public class CustomException {
    private final String message;

    public CustomException(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
