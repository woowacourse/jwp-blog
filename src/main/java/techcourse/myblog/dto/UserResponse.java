package techcourse.myblog.dto;

public class UserResponse {

    private String name;
    private String email;

    public UserResponse() {
    }

    public UserResponse(String name, String email) {
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
