package techcourse.myblog.user;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.dto.request.UserChangeableInfoDto;
import techcourse.myblog.dto.request.UserSignUpInfoDto;

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

	private String faceBookUrl;

	private Information() {
	}

	public Information(UserSignUpInfoDto userSignUpInfoDto) {
		this.email = userSignUpInfoDto.getEmail();
		this.username = userSignUpInfoDto.getUsername();
		this.password = userSignUpInfoDto.getPassword();
	}

	//todo: 업성도 될듯
	public Information(String email) {
		this.email = email;
	}

	public Information(String email, String password, UserChangeableInfoDto userChangeableInfoDto) {
		this.email = email;
		this.password = password;
		this.username = userChangeableInfoDto.getUsername();
		this.githubUrl = userChangeableInfoDto.getGithubUrl();
		this.faceBookUrl = userChangeableInfoDto.getFaceBookUrl();
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
		return faceBookUrl;
	}

	@Override
	public String toString() {
		return "Information{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", githubUrl='" + githubUrl + '\'' +
				", faceBookUrl='" + faceBookUrl + '\'' +
				'}';
	}
}
