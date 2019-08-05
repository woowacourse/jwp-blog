package techcourse.myblog.exception;

public class UserNotMatchedException extends IllegalArgumentException {
    public UserNotMatchedException() {
        super("not matched user");
    }
}
