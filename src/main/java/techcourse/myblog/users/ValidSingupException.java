package techcourse.myblog.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidSingupException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ValidSingupException.class);

    private Error[] errors;

    public ValidSingupException(String defaultMessage, String field) {
        this.errors = new Error[]{new Error(defaultMessage, field)};
        log.debug("field : {}, message : {}", field, defaultMessage);
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
