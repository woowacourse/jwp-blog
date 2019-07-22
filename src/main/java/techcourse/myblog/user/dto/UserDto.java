package techcourse.myblog.user.dto;

import lombok.Data;
import techcourse.myblog.user.domain.User;

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
    public static class Update {
        @Pattern(regexp = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}",
                message = "올바른 이름 형식이 아닙니다.")
        private String name;

        public User toUser(long id, String email, String password) {
            return User.builder()
                    .id(id)
                    .name(name)
                    .email(email)
                    .password(password)
                    .build();
        }
    }

    @Data
    public static class Response {
        private long id;
        private String email;
        private String name;
    }

    @Data
    public static class Login {
        private String email;
        private String password;
    }
}
