package techcourse.myblog.service;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;

public class UserAssembler {
    public static UserResponseDto convertToDto(User user) {
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();

        return new UserResponseDto(email, name, password);
    }

    public static User convertToEntity(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail();
        String name = userRequestDto.getName();
        String password = userRequestDto.getPassword();

        return new User(email, name, password);
    }
}
