package techcourse.myblog.exception;

public class UserUpdateException extends IllegalArgumentException {
    private static final String EDIT_URL = "/users/edit/";

    private String url;

    public UserUpdateException() {
        super("User 클래스 변경 중 예외 발생");
    }

    public UserUpdateException(final String message) {
        super(message);
    }

    public UserUpdateException(final String message, final long id) {
        this(message);
        url = EDIT_URL + id;
    }

    public String getUrl() {
        return url;
    }
}
