package techcourse.myblog.service.exception;

public class DuplicatedUserException extends UserLoginException {

    private static final String NAME = "email";

    public DuplicatedUserException(String message) {
        super(message);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
