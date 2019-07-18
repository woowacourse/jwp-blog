package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import techcourse.myblog.dto.UserDto;

@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;

	private String username;
	private String password;
	private String email;

	public User() {}

	public User(UserDto userDto) {
		saveUser(userDto);
	}

	public void saveUser(UserDto userDto) {
		this.username = userDto.getUsername();
		this.password = userDto.getPassword();
		this.email = userDto.getEmail();
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
}
