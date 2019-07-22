package techcourse.myblog.domain;

import techcourse.myblog.dto.UserDto;

public class UserAssembler {
    public static User writeUser(UserDto userDto) {
        return new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }
}
