package techcourse.myblog.dto;

public class SignUpDto {
    private static final String SUCCESS = "success";
    private static final long DEFAULT_ID = -1L;
    private final boolean success;
    private final Long id;
    private final String message;

    private SignUpDto(boolean success, Long id, String message) {
        this.success = success;
        this.id = id;
        this.message = message;
    }

    public SignUpDto(boolean success, Long id) {
        this(success, id, SUCCESS);
    }

    public SignUpDto(boolean success, String message) {
        this(success, DEFAULT_ID, message);
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