package techcourse.myblog.dto;

import techcourse.myblog.domain.User;

public class UserDto {
    private String name;
    private String password;
    private String email;

    public UserDto() {
    }

    public UserDto(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserDto(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toEntity() {
        return new User(this.name, this.password, this.email);
    }

}
