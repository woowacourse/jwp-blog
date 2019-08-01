package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.domain.User;

public class UserConverter implements Converter<UserRequestDto, UserResponseDto, User> {
    private static UserConverter converter = new UserConverter();

    public static UserConverter getInstance() {
        return converter;
    }

    @Override
    public User convertFromDto(UserRequestDto dto) {
        return new User(dto.getEmail()
                , dto.getName()
                , dto.getPassword());
    }

    @Override
    public UserResponseDto convertFromEntity(User entity) {
        return new UserResponseDto(entity.getId(), entity.getEmail(), entity.getName());
    }
}