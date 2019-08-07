package techcourse.myblog.service.dto.domain;

import techcourse.myblog.domain.User;

public class UserDTO implements DomainDTO<User> {
    public UserDTO(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    private String userName;
    private String email;
    private String password;

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public User toDomain() {
        return new User(userName, email, password);
    }
}
