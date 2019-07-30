package techcourse.myblog.error;

public class BadRequestError implements RequestError {
    private static final String ERROR_MESSAGE_SUFFIX = "는 잘못된 요청입니다.";

    private String errorMessage;

    public BadRequestError(Object cause) {
        this.errorMessage = cause.toString() + ERROR_MESSAGE_SUFFIX;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
