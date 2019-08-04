package techcourse.myblog.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.user.Information;
import techcourse.myblog.user.User;

public class UserChangeableInfoDto {
	private static final String BLANK_NAME = "이름을 입력해주세요.";

	@Length(min = 2, max = 10)
	@NotBlank(message = BLANK_NAME)
	@Pattern(regexp = "^[a-zA-Z가-힣]+$")
	private String username;

	private String githubUrl;
	private String faceBookUrl;

	public UserChangeableInfoDto(String username, String githubUrl, String faceBookUrl) {
		this.username = username;
		this.githubUrl = githubUrl;
		this.faceBookUrl = faceBookUrl;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getFaceBookUrl() {
		return faceBookUrl;
	}

	public String getUsername() {
		return username;
	}

	public Information valueOfInfo(User loginUser) {
		return new Information(loginUser.getEmail(), loginUser.getPassword(), this);
	}
}
