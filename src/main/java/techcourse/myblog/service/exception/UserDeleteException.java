package techcourse.myblog.service.exception;

public class UserDeleteException extends RuntimeException {
    public static final String UPDATE_USER_FAIL_MESSAGE = "User update fail";

    public UserDeleteException(final String message) {
        super(UPDATE_USER_FAIL_MESSAGE + " : " + message);
    }
}
