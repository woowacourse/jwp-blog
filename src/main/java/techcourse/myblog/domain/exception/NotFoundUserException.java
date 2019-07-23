package techcourse.myblog.domain.exception;

public class NotFoundUserException extends UserLoginException {

    private static final String NAME = "email";

    public NotFoundUserException(String message) {
        super(message);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
