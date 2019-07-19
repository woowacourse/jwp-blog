package techcourse.myblog.web;

import techcourse.myblog.domain.User;

import javax.validation.constraints.NotNull;

public class UserDto {

	public static class LoginInfo {

		@NotNull
		private String email;

		@NotNull
		private String password;

		public LoginInfo() {
		}

		public LoginInfo(String email, String password) {
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

	public static class SignUpUserInfo {

		@NotNull
		private String name;

		@NotNull
		private String email;

		@NotNull
		private String password;

		public SignUpUserInfo() {
		}

		public SignUpUserInfo(String name, String email, String password) {
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

	public static class SessionUserInfo {

		@NotNull
		private Long id;

		@NotNull
		private String name;

		@NotNull
		private String email;

		public SessionUserInfo() {

		}

		public SessionUserInfo(final long id, final String name, final String email) {
			this.id = id;
			this.name = name;
			this.email = email;
		}

		public static SessionUserInfo toDto(final User user) {
			return new SessionUserInfo(user.getId(), user.getName(), user.getEmail());
		}

		public boolean isSameId(final Long id) {
			return this.id.compareTo(id) == 0;
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
			return "SessionUserInfo{" +
					"id=" + id +
					", name='" + name + '\'' +
					", email='" + email + '\'' +
					'}';
		}
	}

	public class UpdateInfo {
		@NotNull
		private Long id;

		@NotNull
		private String name;

		@NotNull
		private String email;

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
			return "UpdateInfo{" +
					"id=" + id +
					", name='" + name + '\'' +
					", email='" + email + '\'' +
					'}';
		}
	}
}
