package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.dto.request.UserEditProfileDto;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean matchUser(User user) {
		return this.id.equals(user.id);
	}
}
