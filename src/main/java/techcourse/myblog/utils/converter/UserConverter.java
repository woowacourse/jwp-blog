package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;

public class UserConverter {
    public static User toEntity(UserRequestDto dto) {
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }

    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getName(), user.getEmail());
    }

}
