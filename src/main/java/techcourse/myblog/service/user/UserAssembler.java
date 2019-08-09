package techcourse.myblog.service.user;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import java.util.Objects;

public class UserAssembler {
    public static UserResponse convertToDto(final User user) {
        Objects.requireNonNull(user);

        Long id = user.getId();
        String email = user.getEmail();
        String name = user.getName();
        return new UserResponse(id, email, name);
    }

    public static User convertToEntity(final UserRequest userRequest) {
        Objects.requireNonNull(userRequest);

        String email = userRequest.getEmail();
        String name = userRequest.getName();
        String password = userRequest.getPassword();
        return new User(email, name, password);
    }
}
