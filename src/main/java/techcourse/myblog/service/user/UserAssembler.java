package techcourse.myblog.service.user;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

import java.util.Objects;

public class UserAssembler {
    public static UserResponseDto convertToDto(final User user) {
        Objects.requireNonNull(user);

        Long id = user.getId();
        String email = user.getEmail();
        String name = user.getName();
        return new UserResponseDto(id, email, name);
    }

    public static User convertToEntity(final UserRequestDto userRequestDto) {
        Objects.requireNonNull(userRequestDto);

        String email = userRequestDto.getEmail();
        String name = userRequestDto.getName();
        String password = userRequestDto.getPassword();
        return new User(email, name, password);
    }
}
