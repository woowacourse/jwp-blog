package techcourse.myblog.service.dto;

import techcourse.myblog.domain.User;

public class UserResponse {
    private String name;
    private String email;

    public UserResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getName(), user.getEmail());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
