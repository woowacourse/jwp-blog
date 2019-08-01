package techcourse.myblog.domain.vo.user;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.domain.User;

public class UserSignUpInfo extends UserBasicInfo {
	@Length(min = 8)
	@Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$")
	private String password;

	public UserSignUpInfo(String username, String email, String password) {
		super(username, email);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public User valueOfUser() {
		return new User(this);
	}
}
