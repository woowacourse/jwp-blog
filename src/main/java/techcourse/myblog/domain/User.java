package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User {
    public static final String PASSWORD_PATTERN = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String NAME_PATTERN = "^.{2,10}$";
    public static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_NAME_LENGTH = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_PATTERN, message = "형식에 맞는 이름이 아닙니다.")
    private String name;

    @Column(unique = true)
    @NotBlank
    @Pattern(regexp = EMAIL_PATTERN, message = "email형식에 맞는 이름이 아닙니다.")
    private String email;

    @Column
    @NotBlank
    @Pattern(regexp = PASSWORD_PATTERN, message = "올바른 password에 맞는 형식이 아닙니다.")
    private String password;

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public void updateName(final String name) {
        this.name = name;
    }
}