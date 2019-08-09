package techcourse.myblog.application.converter;

import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;

@Component
public class UserConverter extends Converter<UserDto, User> {
    private UserConverter() {
        super(userDto -> new User(userDto.getEmail(), userDto.getName(), userDto.getPassword())
                , user -> {
                    UserDto userDto = new UserDto(user.getEmail(), user.getName(), user.getPassword());
                    userDto.setId(user.getId());
                    return userDto;
                });
    }
}