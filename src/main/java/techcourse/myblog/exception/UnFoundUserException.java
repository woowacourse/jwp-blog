package techcourse.myblog.exception;

public class UnFoundUserException extends UserLoginException {

    private static final String NAME = "email";

    public UnFoundUserException(String message) {
        super(message);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
