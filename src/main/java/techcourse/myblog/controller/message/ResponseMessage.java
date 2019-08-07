package techcourse.myblog.controller.message;

public class ResponseMessage {
    private String status;
    private String message;
    private String errorMessage;
    private String errorCode;

    public ResponseMessage() {

    }

    public ResponseMessage(String status, String message, String errorMessage, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
