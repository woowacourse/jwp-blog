package techcourse.myblog.service.dto;

import lombok.*;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static techcourse.myblog.domain.UserValidator.*;

public class UserDto {

    @Builder
    @Getter
    @Setter
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

        public boolean isValidPassword() {
            return password.equals(confirmPassword);
        }

        public User toUser() {
            return User.builder()
                    .email(email)
                    .name(name)
                    .password(password)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private Long id;

        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = NAME_REGEX, message = NAME_NOT_MATCH_MESSAGE)
        private String name;

        public User toUser() {
            return User.builder()
                    .name(name)
                    .id(id)
                    .build();
        }

    }
}
