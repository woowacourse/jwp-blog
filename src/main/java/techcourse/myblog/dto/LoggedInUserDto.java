package techcourse.myblog.dto;

public class LoggedInUserDto {
    private String name;
    private String email;

    public LoggedInUserDto(String name, String email) {
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
