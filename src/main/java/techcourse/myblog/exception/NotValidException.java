package techcourse.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// https://jojoldu.tistory.com/129
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidException extends RuntimeException {
    private Error[] errors;

    public NotValidException(final String defaultMessage, final String field) {
        this.errors = new Error[] {new Error(defaultMessage, field)};
    }

    public NotValidException(final Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static class Error {
        private String defaultMessage;
        private String field;

        public Error(final String defaultMessage, final String field) {
            this.defaultMessage = defaultMessage;
            this.field = field;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public String getField() {
            return field;
        }
    }
}
