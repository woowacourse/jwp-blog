package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {
    @Pattern(regexp = "^[a-zA-Z]{2,10}$", message = "형식에 맞는 이름이 아닙니다. 이름은 2~10자의 영어 대소문자만 허용합니다.")
    private String userName;
}
