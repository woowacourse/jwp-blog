package techcourse.myblog.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidUserException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ValidUserException.class);

    private Error[] errors;

    public ValidUserException(String defaultMessage, String field) {
        this.errors = new Error[]{new Error(defaultMessage, field)};
        log.error("field : {}, message : {}", field, defaultMessage);
    }

    public ValidUserException(Error[] errors) {
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
