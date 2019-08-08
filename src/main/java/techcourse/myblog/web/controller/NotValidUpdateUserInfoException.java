package techcourse.myblog.web.controller;

public class NotValidUpdateUserInfoException extends RuntimeException {
    public NotValidUpdateUserInfoException(String message) {
        super(message);
    }

}
