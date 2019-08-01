package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

import static techcourse.myblog.domain.User.*;

@Getter
@Setter
public class UserDto {
    private Long id;

    @Length(min = 2, max = 10)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Length(min = 8)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;

    @Pattern(regexp = EMAIL_PATTERN)
    private String email;
}
