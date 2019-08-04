package techcourse.myblog.domain;

import techcourse.myblog.dto.SignUpRequest;
import techcourse.myblog.dto.UpdateUserRequest;
import techcourse.myblog.dto.UserResponse;

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
        String name = user.getName();
        String email = user.getEmail();

        return new UserResponse(name, email);
    }
}
