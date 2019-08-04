package techcourse.myblog.web.dto;

import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserRequestDto;

import java.util.Objects;

public class UserAssembler {
    public static UserDto convertToDto(final User user) {
        Objects.requireNonNull(user);

        Long id = user.getId();
        String email = user.getEmail();
        String name = user.getName();
        return new UserDto(id, email, name);
    }

    public static User convertToEntity(final UserRequestDto userRequestDto) {
        Objects.requireNonNull(userRequestDto);

        String email = userRequestDto.getEmail();
        String name = userRequestDto.getName();
        String password = userRequestDto.getPassword();
        return new User(email, name, password);
    }
}
