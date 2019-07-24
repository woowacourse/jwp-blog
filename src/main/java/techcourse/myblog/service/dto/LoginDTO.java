package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginDTO {
    private String email;
    private String password;
}
