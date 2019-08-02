package techcourse.myblog.domain.assembler;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequest;
import techcourse.myblog.dto.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {
    public static UserResponse writeDto(User user) {
        UserResponse userDto = new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );

        return userDto;
    }

    public static List<UserResponse> writeDtos(List<User> users) {
        return users.stream()
                .map(UserAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static User writeUser(UserRequest userDto) {
        return new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }
}
