package techcourse.myblog.service.dto;

import techcourse.myblog.domain.user.User;

public class UserDto {
    private String name;
    private String email;
    private String password;
    private String passwordConfirm;

    public UserDto(String name, String email, String password, String passwordConfirm) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
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

    public boolean confirmPassword() {
        return password.equals(passwordConfirm);
    }

    public User toEntity() {
        return new User(name, email, password);
    }
}
