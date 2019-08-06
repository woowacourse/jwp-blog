package techcourse.myblog.domain.user;

import techcourse.myblog.dto.user.SignUpRequest;
import techcourse.myblog.dto.user.UpdateUserRequest;
import techcourse.myblog.dto.user.UserResponse;

public class UserAssembler {
    public static User toEntity(SignUpRequest dto) {
        String name = dto.getName();
        String password = dto.getPassword();
        String email = dto.getEmail();

        return new User(name, password, email);
    }

    public static User toEntity(UpdateUserRequest dto) {
        String name = dto.getName();
        String email = dto.getEmail();

        return new User(name, email);
    }

    public static UserResponse toDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        String email = user.getEmail();

        return new UserResponse(id, name, email);
    }
}
