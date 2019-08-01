package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import techcourse.myblog.domain.vo.user.UserBasicInfo;
import techcourse.myblog.domain.vo.user.UserChangeableInfo;
import techcourse.myblog.domain.vo.user.UserSignUpInfo;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String email;
	private String githubUrl;
	private String facebookUrl;

	private User() {
	}

	public User(UserSignUpInfo userSignUpInfo) {
		this.username = userSignUpInfo.getUsername();
		this.password = userSignUpInfo.getPassword();
		this.email = userSignUpInfo.getEmail();
	}

	public User(UserBasicInfo userBasicInfo) {
		this.username = userBasicInfo.getUsername();
		this.email = userBasicInfo.getEmail();
	}

	public void saveUser(UserSignUpInfo userSignUpInfo) {
		this.username = userSignUpInfo.getUsername();
		this.password = userSignUpInfo.getPassword();
		this.email = userSignUpInfo.getEmail();
	}

	public void editUser(UserChangeableInfo userChangeableInfo) {
		this.username = userChangeableInfo.getUsername();
		this.githubUrl = userChangeableInfo.getGithubUrl();
		this.facebookUrl = userChangeableInfo.getFaceBookUrl();
	}

	public boolean matchPassword(String password) {
		return this.password.equals(password);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public Long getId() {
		return id;
	}

	public boolean matchUser(User user) {
		return this.id.equals(user.id);
	}
}
