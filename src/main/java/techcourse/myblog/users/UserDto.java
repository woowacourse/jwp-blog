package techcourse.myblog.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
    private static final String NAME_PATTERN = "[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]{2,10}";
    static final String EMAIL_NOT_MATCH_MESSAGE = "메일의 양식을 지켜주세요.";
    static final String NAME_NOT_MATCH_MESSAGE = "이름은 2자이상 10자이하이며, 숫자나 특수문자가 포함될 수 없습니다.";
    static final String PASSWORD_NOT_MATCH_MESSAGE = "비밀번호는 8자 이상의 소문자,대문자,숫자,특수문자의 조합이여야 합니다.";

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotBlank(message = "메일을 작성해주세요.")
        @Email(message = EMAIL_NOT_MATCH_MESSAGE)
        private String email;

        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = NAME_PATTERN, message = NAME_NOT_MATCH_MESSAGE)
        private String name;

        @NotBlank(message = "패스워드를 입력해주세요.")
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_NOT_MATCH_MESSAGE)
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
        @Pattern(regexp = NAME_PATTERN, message = NAME_NOT_MATCH_MESSAGE)
        private String name;

        public User toUser() {
            return User.builder()
                    .name(name)
                    .build();
        }
    }
}
