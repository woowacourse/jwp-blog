package techcourse.myblog.application.dto;

import techcourse.myblog.application.exception.JsonAPIException;

public class ErrorResponse extends BaseResponse {

    private String message;

    public ErrorResponse() {
        super("fail");
    }

    public ErrorResponse(JsonAPIException e) {
        super("fail");
        message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
