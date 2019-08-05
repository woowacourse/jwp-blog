package techcourse.myblog.application.dto;

import techcourse.myblog.application.exception.EmptyCommentRequestException;

public class ErrorResponse extends BaseResponse {

    private final String message;

    public ErrorResponse(EmptyCommentRequestException e) {
        super("fail");
        message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
