package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.User;

@Getter
public class UserResponse {
    private Long id;
    private String name;

    private UserResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserResponse of(User writer) {
        return new UserResponse(writer.getId(), writer.getName());
    }
}
