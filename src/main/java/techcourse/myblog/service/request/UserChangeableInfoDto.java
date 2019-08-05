package techcourse.myblog.service.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;

public class UserChangeableInfoDto {
	private static final String BLANK_NAME = "이름을 입력해주세요.";

	@Length(min = 2, max = 10)
	@NotBlank(message = BLANK_NAME)
	@Pattern(regexp = "^[a-zA-Z가-힣]+$")
	private String username;

	private String githubUrl;
	private String facebookUrl;

	public UserChangeableInfoDto(String username, String githubUrl, String facebookUrl) {
		this.username = username;
		this.githubUrl = githubUrl;
		this.facebookUrl = facebookUrl;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public String getUsername() {
		return username;
	}

	public Information valueOfInfo(User loginUser) {
		return new Information(loginUser.getEmail(), username, loginUser.getPassword(), githubUrl, facebookUrl);
	}
}
