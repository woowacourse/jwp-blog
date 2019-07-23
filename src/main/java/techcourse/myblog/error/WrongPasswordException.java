package techcourse.myblog.error;

public class WrongPasswordException  extends RuntimeException {
    public WrongPasswordException(String msg) {
        super(msg);
    }
}