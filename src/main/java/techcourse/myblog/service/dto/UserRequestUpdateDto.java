package techcourse.myblog.service.dto;

import techcourse.myblog.domain.user.User;

public class UserRequestUpdateDto {
    private static final String DEFAULT_PASSWORD = "passWORD1!";

    private String name;
    private String email;

    public UserRequestUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User toEntity() {
        return new User(name, email, DEFAULT_PASSWORD);
    }
}
