package techcourse.myblog.application.exception;

public class JsonAPIException extends RuntimeException {

    public JsonAPIException(String message) {
        super(message);
    }
}
