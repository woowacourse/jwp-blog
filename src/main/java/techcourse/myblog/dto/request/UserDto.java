package techcourse.myblog.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class UserDto {
	public static final String BLANK_NAME = "이름을 입력해주세요.";
	public static final String NOT_VALID_EMAIL = "올바른 이메일 주소를 입력해주세요.";

	@Length(min = 2, max = 10)
	@NotBlank(message = BLANK_NAME)
	@Pattern(regexp = "^[a-zA-Z가-힣]+$")
	private String username;

	@Length(min = 8)
	@Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$")
	private String password;

	@NotBlank(message = NOT_VALID_EMAIL)
	@Email(message = NOT_VALID_EMAIL)
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserDto{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
