package techcourse.myblog.exception;

public class NotMatchPasswordException extends UserLoginException {

    private static final String NAME = "password";

    public NotMatchPasswordException(String message) {
        super(message);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
