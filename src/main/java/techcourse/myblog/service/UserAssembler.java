package techcourse.myblog.service;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;

public class UserAssembler {
    public static UserDto convertToDto(User user) {
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();

        return new UserDto(email, name, password);
    }

    public static User convertToEntity(UserDto userDto) {
        String email = userDto.getEmail();
        String name = userDto.getName();
        String password = userDto.getPassword();

        return new User(email, name, password);
    }
}
