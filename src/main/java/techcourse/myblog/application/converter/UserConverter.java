package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;

public class UserConverter extends Converter<UserDto, User> {

    private static class UserConverterHolder {
        private static final UserConverter INSTANCE = new UserConverter();
    }

    public static UserConverter getInstance() {
        return UserConverterHolder.INSTANCE;
    }

    private UserConverter() {
        super(userDto -> new User(
                userDto.getEmail(),
                userDto.getName(),
                userDto.getPassword()),
                UserDto::of);
    }
}
