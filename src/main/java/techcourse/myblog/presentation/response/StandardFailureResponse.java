package techcourse.myblog.presentation.response;

public class StandardFailureResponse extends StandardResponse {
    private Integer code;

    public StandardFailureResponse(String message, Object data, int code) {
        super(false, message, data);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
