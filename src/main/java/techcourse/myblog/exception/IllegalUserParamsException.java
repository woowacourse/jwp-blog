package techcourse.myblog.exception;

public class IllegalUserParamsException extends IllegalArgumentException {
    public IllegalUserParamsException() {
    }

    public IllegalUserParamsException(String s) {
        super(s);
    }
}
