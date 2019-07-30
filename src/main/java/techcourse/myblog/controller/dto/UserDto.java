package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$", message = "2글자 이상, 10글자 이하로 입력하세요. 숫자와 특수문자는 입력할 수 없습니다.")
    private String name;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,}$", message = "소문자, 대문자, 숫자, 특수문자가 조합된 8글자 이상을 입력하세요 ")
    private String password;
    @Email(message = "Wrong Email")
    private String email;

    public User toUser() {
        if (id != 0) {
            return new User(id, name, password, email);
        }
        return new User(name, password, email);
    }
}
