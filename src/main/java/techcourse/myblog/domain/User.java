package techcourse.myblog.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.dto.request.UserEditProfileDto;
//TODO: @COLUMN 추가하기
@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;

	private String username;
	private String password;
	private String email;
	private String githubURL;
	private String facebookURL;

	public User() {
	}

	public void saveUser(UserDto userDto) {
		this.username = userDto.getUsername();
		this.password = userDto.getPassword();
		this.email = userDto.getEmail();
	}

	public void editUser(UserEditProfileDto userEditProfileDto) {
		this.username = userEditProfileDto.getUsername();
		this.githubURL = userEditProfileDto.getGithubURL();
		this.facebookURL = userEditProfileDto.getFacebookURL();
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

	public String getGithubURL() {
		return githubURL;
	}

	public String getFacebookURL() {
		return facebookURL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		final User user = (User) o;
		return Objects.equals(id, user.id) &&
				Objects.equals(getUsername(), user.getUsername()) &&
				Objects.equals(getPassword(), user.getPassword()) &&
				Objects.equals(getEmail(), user.getEmail()) &&
				Objects.equals(getGithubURL(), user.getGithubURL()) &&
				Objects.equals(getFacebookURL(), user.getFacebookURL());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, getUsername(), getPassword(), getEmail(), getGithubURL(), getFacebookURL());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", githubURL='" + githubURL + '\'' +
				", facebookURL='" + facebookURL + '\'' +
				'}';
	}
}
