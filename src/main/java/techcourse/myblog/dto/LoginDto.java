package techcourse.myblog.dto;

public class LoginDto {
    private final boolean success;
    private final String message;
    private final String name;

    public LoginDto(boolean success, String message, String name) {
        this.success = success;
        this.message = message;
        this.name = name;
    }

    public LoginDto(boolean success, String message) {
        this(success, message, "NotFound");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
