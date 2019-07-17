package techcourse.myblog.dto;

import lombok.Data;
import techcourse.myblog.domain.User;

public class UserDto {

    @Data
    public static class Create {
        private String email;
        private String password;
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
}
