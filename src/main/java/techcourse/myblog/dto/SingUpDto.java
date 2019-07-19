package techcourse.myblog.dto;

public class SingUpDto {
    private static final String SUCCESS = "success";
    private static final long DEFAULT_ID = -1L;
    private final boolean success;
    private final Long id;
    private final String message;

    private SingUpDto(boolean success, Long id, String message) {
        this.success = success;
        this.id = id;
        this.message = message;
    }

    public SingUpDto(boolean success, Long id) {
        this(success, id, SUCCESS);
    }

    public SingUpDto(boolean success, String message) {
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