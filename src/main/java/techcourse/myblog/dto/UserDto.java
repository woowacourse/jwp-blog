package techcourse.myblog.dto;

import lombok.Data;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class UserDto {

    @Data
    public static class Create {
        @Email(message = "올바른 email 형식이 아닙니다.")
        private String email;

        @Pattern(regexp = ".*(?=^.{8,}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*",
                message = "올바른 비밀번호 형식이 아닙니다.")
        private String password;

        @Pattern(regexp = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}",
                message = "올바른 이름 형식이 아닙니다.")
        private String name;

        public User toUser() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .build();
        }
    }

    @Data
    public static class Response {
        private String email;
        private String name;
    }

    @Data
    public static class Login {
        private String email;
        private String password;
    }
}
