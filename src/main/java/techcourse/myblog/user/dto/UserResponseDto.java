package techcourse.myblog.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserResponseDto {
    private long id;
    private String email;
    private String name;

    @Builder
    public UserResponseDto(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
