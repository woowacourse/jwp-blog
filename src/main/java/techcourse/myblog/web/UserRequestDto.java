package techcourse.myblog.web;

import techcourse.myblog.domain.User.User;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserRequestDto {

    public static class LoginRequestDto {

        @NotNull
        private String email;

        @NotNull
        private String password;

        public LoginRequestDto() {
        }

        public LoginRequestDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class SignUpRequestDto {

        @NotNull
        private String name;

        @NotNull
        private String email;

        @NotNull
        private String password;

        public SignUpRequestDto() {
        }

        public SignUpRequestDto(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public User toUser() {
            return new User(name, email, password);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class SessionDto {

        @NotNull
        private Long id;

        @NotNull
        private String name;

        @NotNull
        private String email;

        @NotNull
        private String password;

        public SessionDto() {

        }

        public SessionDto(final Long id, final String name, final String email, final String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public static SessionDto toDto(final User user) {
            return new SessionDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }


        public String getEmail() {
            return email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SessionDto)) return false;
            SessionDto that = (SessionDto) o;
            return Objects.equals(email, that.email) &&
                    Objects.equals(password, that.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email, password);
        }

        @Override
        public String toString() {
            return "SessionDto{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class UpdateRequestDto {

        @NotNull
        private Long id;

        @NotNull
        private String name;

        @NotNull
        private String email;

        public UpdateRequestDto() {

        }

        public UpdateRequestDto(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "UpdateRequestDto{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
