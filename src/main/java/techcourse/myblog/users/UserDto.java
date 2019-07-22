package techcourse.myblog.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static techcourse.myblog.users.User.*;

public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotBlank(message = "메일을 작성해주세요.")
        @Email(message = EMAIL_NOT_MATCH_MESSAGE)
        private String email;

        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = NAME_REGEX, message = NAME_NOT_MATCH_MESSAGE)
        private String name;

        @NotBlank(message = "패스워드를 입력해주세요.")
        @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_NOT_MATCH_MESSAGE)
        private String password;

        @NotBlank(message = "패스워드 확인을 입력해주세요.")
        private String confirmPassword;

        boolean isValidPassword() {
            return password.equals(confirmPassword);
        }

        User toUser() {
            return User.builder()
                    .email(email)
                    .name(name)
                    .password(password)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String email;
        private String name;

        Response(User user) {
            id = user.getId();
            email = user.getEmail();
            name = user.getName();
        }

        public static Response createByUser(User user) {
            return new Response(user);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = NAME_REGEX, message = NAME_NOT_MATCH_MESSAGE)
        private String name;

        public User toUser() {
            return new User(name);
        }

    }
}
