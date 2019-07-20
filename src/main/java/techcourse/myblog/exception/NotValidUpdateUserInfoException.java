package techcourse.myblog.exception;

import java.net.BindException;

public class NotValidUpdateUserInfoException extends BindException {
    public NotValidUpdateUserInfoException(String message) {
        super(message);
    }

}
