package techcourse.myblog.domain.vo.user;

public class UserChangeableInfo extends UserBasicInfo {
	private String githubUrl;
	private String faceBookUrl;

	public UserChangeableInfo(String username, String email, String githubUrl, String faceBookUrl) {
		super(username, email);
		this.githubUrl = githubUrl;
		this.faceBookUrl = faceBookUrl;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getFaceBookUrl() {
		return faceBookUrl;
	}
}
