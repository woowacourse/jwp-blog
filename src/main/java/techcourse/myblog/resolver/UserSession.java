package techcourse.myblog.resolver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import techcourse.myblog.dto.UserDto;

@RequiredArgsConstructor
@Getter
public class UserSession {
    private final UserDto.Response userDto;
}
