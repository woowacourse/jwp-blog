package techcourse.myblog.dto;

public class UserPublicInfoDto {
    private String name;
    private String email;

    public UserPublicInfoDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
