package techcourse.myblog.argumentresolver;

import lombok.Getter;
import techcourse.myblog.user.dto.UserResponseDto;

@Getter
public class UserSession {
    private long id;
    private String email;
    private String name;

    public UserSession(UserResponseDto userResponseDto) {
        this.id = userResponseDto.getId();
        this.email = userResponseDto.getEmail();
        this.name = userResponseDto.getName();
    }
}
