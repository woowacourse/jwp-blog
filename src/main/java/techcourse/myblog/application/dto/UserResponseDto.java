package techcourse.myblog.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String password;
}
