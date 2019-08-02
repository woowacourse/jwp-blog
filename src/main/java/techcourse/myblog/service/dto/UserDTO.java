package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import techcourse.myblog.domain.User;

@AllArgsConstructor
@Getter
public class UserDTO implements DomainDTO<User> {
    private String userName;
    private String email;
    private String password;

    @Override
    public User toDomain() {
        return new User(userName, email, password);
    }
}
