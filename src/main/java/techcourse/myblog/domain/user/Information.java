package techcourse.myblog.domain.user;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class Information {
	public static final String BLANK_NAME = "이름을 입력해주세요.";
	public static final String NOT_VALID_EMAIL = "올바른 이메일 주소를 입력해주세요.";

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

	private String githubUrl;

	private String facebookUrl;

	private Information() {
	}

	public Information(String email) {
		this.email = email;
	}

	public Information(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Information(String email, String username, String password, String githubUrl, String facebookUrl) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.githubUrl = githubUrl;
		this.facebookUrl = facebookUrl;
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

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	@Override
	public String toString() {
		return "Information{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", githubUrl='" + githubUrl + '\'' +
				", facebookUrl='" + facebookUrl + '\'' +
				'}';
	}
}
