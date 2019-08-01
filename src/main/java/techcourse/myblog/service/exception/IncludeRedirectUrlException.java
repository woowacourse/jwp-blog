package techcourse.myblog.service.exception;

public class IncludeRedirectUrlException extends RuntimeException {
    private String redirectUrl;

    public IncludeRedirectUrlException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
