package techcourse.myblog.user.service;

import techcourse.myblog.user.User;
import techcourse.myblog.user.dto.UserRequest;
import techcourse.myblog.user.dto.UserResponse;

import java.util.Objects;

public class UserAssembler {
    private static final long TEMP_ID = 0L;

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
        return new User(TEMP_ID, email, name, password);
    }
}
