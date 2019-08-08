package techcourse.myblog.web.dto;

import techcourse.myblog.application.exception.JsonAPIException;
import techcourse.myblog.web.dto.BaseResponse;

public class ErrorResponse extends BaseResponse {

    private String message;

    public ErrorResponse() {
        super("fail");
    }

    public ErrorResponse(JsonAPIException e) {
        super("fail");
        message = e.getMessage();
    }

    public ErrorResponse(String message) {
        super("fail");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static enum ErrorResult {
        FAIL("fail"), ERROR("error");

        private String result;

        ErrorResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return result;
        }
    }
}
