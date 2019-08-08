package techcourse.myblog.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.user.User;

@Getter
@Setter
public class LoginUser {
	private String userName;
	private String email;

	public LoginUser(String userName, String email) {
		this.userName = userName;
		this.email = email;
	}

	public static LoginUser toLoginUser(User user) {
		return new LoginUser(user.getUserName(), user.getEmail());
	}

	@Override
	public String toString() {
		return "LoginUser{" +
				"userName='" + userName + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
