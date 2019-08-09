package techcourse.myblog.web.dto;

import techcourse.myblog.application.exception.JsonAPIException;

public class ErrorResponse {

    private ErrorResult result;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        result = ErrorResult.fail;
        this.message = message;
    }

    public static ErrorResponse fail(JsonAPIException e) {
        ErrorResponse res = new ErrorResponse();
        res.result = ErrorResult.fail;
        res.message = e.getMessage();
        return res;
    }

    public static ErrorResponse error(Exception e) {
        ErrorResponse res = new ErrorResponse();
        res.result = ErrorResult.error;
        res.message = e.getMessage();
        return res;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResult getResult() {
        return result;
    }

    public enum ErrorResult {
        // FAIL => "FAIL"로 변환되기 때문에 소문자 사용
        fail, error;

    }
}
