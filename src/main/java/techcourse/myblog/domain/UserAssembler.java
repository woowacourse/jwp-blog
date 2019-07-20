package techcourse.myblog.domain;

import techcourse.myblog.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {
    public static UserDto writeDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setUserId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());

        return userDto;
    }

    public static List<UserDto> writeDtos(List<User> users) {
        return users.stream()
                .map(UserAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static User writeUser(UserDto userDto) {
        return new User(userDto.getUserId(), userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }
}
