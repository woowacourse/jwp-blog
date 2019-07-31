package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;

public class UserConverter {
    public static User convert(UserRequestDto dto) {
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }

    public static UserResponseDto convert(User user) {
        return new UserResponseDto(user.getName(), user.getEmail());
    }

}
