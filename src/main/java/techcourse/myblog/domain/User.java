package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor()
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NonNull
    @Size(min = 2, max = 10)
    private String name;

    @Column(nullable = false)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public String updateName(UserDto userDto) {
        return this.name = userDto.getName();
    }
}