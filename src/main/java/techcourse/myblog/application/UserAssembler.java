package techcourse.myblog.application;

import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.domain.user.User;

public class UserAssembler {
    public static UserResponseDto buildUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(UserRequestDto userRequestDto) {
        return new User(userRequestDto.getName(), userRequestDto.getEmail(), userRequestDto.getPassword());
    }
}
