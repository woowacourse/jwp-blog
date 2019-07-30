package techcourse.myblog.error;

public class NotExistEntityError implements RequestError {
    private static final String ERROR_MESSAGE_SUFFIX = "는 없는 Entity 입니다.";

    private String errorMessage;

    public NotExistEntityError(Object cause) {
        this.errorMessage = cause.toString() + ERROR_MESSAGE_SUFFIX;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
