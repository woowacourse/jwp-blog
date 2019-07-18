package techcourse.myblog.domain;

import techcourse.myblog.dto.UserDto;

public class UserAssembler {
    public static UserDto writeDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setUserId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public static User writeUser(UserDto userDto) {
        return new User(userDto.getUserId(), userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }
}
