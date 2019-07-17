package techcourse.myblog.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidSingupException extends RuntimeException {
    private Error[] errors;

    public ValidSingupException(String defaultMessage, String field) {
        this.errors = new Error[]{new Error(defaultMessage, field)};
    }

    public ValidSingupException(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static class Error {
        private final String defaultMessage;
        private final String field;

        public Error(String defaultMessage, String field) {
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
