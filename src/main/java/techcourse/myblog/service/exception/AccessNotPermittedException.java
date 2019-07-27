package techcourse.myblog.service.exception;

public class AccessNotPermittedException extends RuntimeException{
    public static final String PERMISSION_FAIL_MESSAGE = "Not permitted access";

    public AccessNotPermittedException(String message) {
        super(message);
    }
}
