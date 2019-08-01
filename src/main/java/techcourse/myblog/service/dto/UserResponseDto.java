package techcourse.myblog.service.dto;

import lombok.Getter;
import techcourse.myblog.domain.User;

@Getter
public class UserResponseDto {
    private long id;
    private String name;
    private String email;

    public UserResponseDto(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
