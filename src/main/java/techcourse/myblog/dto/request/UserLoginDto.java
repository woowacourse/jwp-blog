package techcourse.myblog.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import static techcourse.myblog.dto.request.UserDto.NOT_VALID_EMAIL;

public class UserLoginDto {
	@NotBlank(message = NOT_VALID_EMAIL)
	@Email(message = NOT_VALID_EMAIL)
	private String email;

	@Length(min = 8)
	@Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$")
	private String password;

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
	public String
	toString() {
		return "UserLoginDto{" +
				"password='" + password + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
