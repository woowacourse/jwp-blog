package techcourse.myblog.presentation.response;

public class StandardSuccessResponse extends StandardResponse{
    public StandardSuccessResponse(String message, Object data) {
        super(true, message, data);
    }
}
