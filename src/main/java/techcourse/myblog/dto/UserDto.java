package techcourse.myblog.dto;

import techcourse.myblog.domain.User;

public class UserDto {
    private String name;
    private String email;
    private String password;

    public UserDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toEntity() {
        return new User(name, email, password);
    }
}
