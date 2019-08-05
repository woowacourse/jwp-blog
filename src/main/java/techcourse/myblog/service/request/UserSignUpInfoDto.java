package techcourse.myblog.service.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.domain.user.Information;

public class UserSignUpInfoDto {
	private static final String BLANK_NAME = "이름을 입력해주세요.";
	private static final String NOT_VALID_EMAIL = "올바른 이메일 주소를 입력해주세요.";

	@Length(min = 2, max = 10)
	@NotBlank(message = BLANK_NAME)
	@Pattern(regexp = "^[a-zA-Z가-힣]+$")
	private String username;

	@NotBlank(message = NOT_VALID_EMAIL)
	@Email(message = NOT_VALID_EMAIL)
	private String email;

	@Length(min = 8)
	@Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$")
	private String password;

	public UserSignUpInfoDto(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Information valueOfInfo() {
		return new Information(email, username, password);
	}
}
