package techcourse.myblog.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private long id;
    private String email;
    private String name;
}
