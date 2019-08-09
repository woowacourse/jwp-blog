package techcourse.myblog.presentation.response;

public abstract class StandardResponse {
    private Boolean success;
    private String message;
    private Object data;

    public StandardResponse(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static StandardResponse success(String message, Object object) {
        return new StandardSuccessResponse(message, object);
    }

    public static StandardResponse fail(String message, Object object, int code) {
        return new StandardFailureResponse(message, object, code);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
