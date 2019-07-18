package techcourse.myblog.dto;

public class SignupDto {
    private final boolean success;
    private final Long id;
    private final String message;

    public SignupDto(boolean success, Long id, String message) {
        this.success = success;
        this.id = id;
        this.message = message;
    }

    public SignupDto(boolean success, Long id) {
        this(success, id, "success");
    }

    public SignupDto(boolean success, String message) {
        this(success, -1L, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
