package techcourse.myblog.user.dto;

import lombok.*;
import techcourse.myblog.user.domain.User;

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

    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail().getEmail())
                .name(user.getName().getName())
                .build();
    }
}
