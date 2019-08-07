package techcourse.myblog.service.dto;

public class LogInInfoDto {
	private String email;
	private String password;

	public LogInInfoDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
