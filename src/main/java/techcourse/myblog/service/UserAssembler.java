package techcourse.myblog.service;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;

import java.util.Objects;

public class UserAssembler {
    public static UserResponseDto convertToDto(final User user) {
        if (Objects.isNull(user)) {
            throw new NullPointerException();
        }
        String email = user.getEmail();
        String name = user.getName();
        return new UserResponseDto(email, name);
    }

    public static User convertToEntity(final UserRequestDto userRequestDto) {
        if (Objects.isNull(userRequestDto)) {
            throw new NullPointerException();
        }
        String email = userRequestDto.getEmail();
        String name = userRequestDto.getName();
        String password = userRequestDto.getPassword();
        return new User(email, name, password);
    }
}
