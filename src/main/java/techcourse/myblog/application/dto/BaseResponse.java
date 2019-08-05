package techcourse.myblog.application.dto;

public class BaseResponse {

    private String result;

    public BaseResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
