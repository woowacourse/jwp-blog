package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDTO {
    private String userName;
    private String email;
    private String password;
}
