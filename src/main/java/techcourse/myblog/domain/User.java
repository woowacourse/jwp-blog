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
	private String githubUrl;
	private String facebookUrl;

	private User() {
	}

	public User(UserDto userDto) {
		this.username = userDto.getUsername();
		this.password = userDto.getPassword();
		this.email = userDto.getEmail();
	}

	public void saveUser(UserDto userDto) {
		this.username = userDto.getUsername();
		this.password = userDto.getPassword();
		this.email = userDto.getEmail();
	}

	public void editUser(UserEditProfileDto userEditProfileDto) {
		this.username = userEditProfileDto.getUsername();
		this.githubUrl = userEditProfileDto.getGithubUrl();
		this.facebookUrl = userEditProfileDto.getFaceBookUrl();
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
