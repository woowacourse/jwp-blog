package techcourse.myblog.dto.user;

public class UserResponseDto {
    private String email;
    private String name;

    public UserResponseDto(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
