package techcourse.myblog.web.controller.exception;

public class NotValidUpdateUserInfoException extends RuntimeException {
    public NotValidUpdateUserInfoException(String message) {
        super(message);
    }

}