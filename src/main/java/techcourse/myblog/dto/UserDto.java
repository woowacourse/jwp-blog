package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDto {
    private Long id;
    @Length(min = 2, max = 10)
    @Pattern(regexp = "[a-zA-Z가-힣]{2,10}")
    private String name;
    @Length(min = 8)
    @Pattern(regexp = "\\w{8,}")
    private String password;
    @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b")
    private String email;
}
