package techcourse.myblog.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidSingupException extends RuntimeException {

    public ValidSingupException(String message) {
        super(message);
    }

}
