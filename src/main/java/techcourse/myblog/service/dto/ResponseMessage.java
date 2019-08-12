package techcourse.myblog.service.dto;

public class ResponseMessage<T> {
    private T data;
    private String message;
    private String errorMessage;
    private String errorCode;

    public ResponseMessage() {

    }

    public ResponseMessage(String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public ResponseMessage(T data, String message, String errorMessage) {
        this.data = data;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
