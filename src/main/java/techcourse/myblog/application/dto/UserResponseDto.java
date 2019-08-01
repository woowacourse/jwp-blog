package techcourse.myblog.application.dto;

public class UserResponseDto extends UserDto {
    private Long id;

    public UserResponseDto(Long id, String email, String name) {
        super(email, name);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
