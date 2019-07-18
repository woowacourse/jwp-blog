package techcourse.myblog.web;

import techcourse.myblog.domain.User;

public class UserDto {

    public static class LoginInfo {
        private String email;
        private String password;

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

    public static class SignUpUserInfo {
        private String name;
        private String email;
        private String password;

        User toUser() {
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

    public static class SessionUserInfo {
        private Long id;
        private String name;
        private String email;

        private SessionUserInfo(final long id, final String name, final String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public static SessionUserInfo toDto(final User user) {
            return new SessionUserInfo(user.getId(), user.getName(), user.getEmail());
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
    }
}
