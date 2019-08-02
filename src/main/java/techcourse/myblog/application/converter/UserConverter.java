package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;

public class UserConverter extends Converter<UserDto, User> {

    private UserConverter() {
        super(userDto -> new User(
                        userDto.getEmail(),
                        userDto.getName(),
                        userDto.getPassword()),
                UserDto::of);
    }

    public static UserConverter getInstance() {
        return UserConverterHolder.INSTANCE;
    }

    private static class UserConverterHolder {
        private static final UserConverter INSTANCE = new UserConverter();
    }
}