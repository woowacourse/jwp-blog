package techcourse.myblog.exception;

public class InvalidUserDataException extends IllegalArgumentException {
    public InvalidUserDataException() {
        super();
    }

    public InvalidUserDataException(String s) {
        super(s);
    }
}
