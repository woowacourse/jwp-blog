package techcourse.myblog.dto;

public class UserResponseDto {
    private String email;
    private String name;

    public UserResponseDto(String email, String name) {
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
