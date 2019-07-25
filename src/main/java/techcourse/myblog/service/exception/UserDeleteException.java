package techcourse.myblog.service.exception;

public class UserDeleteException extends RuntimeException {
    public static final String DELETE_USER_FAIL_MESSAGE = "User delete fail";

    public UserDeleteException(final String message) {
        super(DELETE_USER_FAIL_MESSAGE + " : " + message);
    }
}
