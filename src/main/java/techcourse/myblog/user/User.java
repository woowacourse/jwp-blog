package techcourse.myblog.user;

import javax.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	@AttributeOverride(name = "username", column = @Column(name = "username"))
	@AttributeOverride(name = "email", column = @Column(name = "email"))
	@AttributeOverride(name = "password", column = @Column(name = "password"))
	@AttributeOverride(name = "githubUrl", column = @Column(name = "githubUrl"))
	@AttributeOverride(name = "facebookUrl", column = @Column(name = "facebookUrl"))
	private Information information;

	private User() {
	}

	public User(final Information information) {
		this.information = information;
	}

	public void editUser(Information information) {
		this.information.update(information);
	}

	public boolean matchUser(User user) {
		return this.id.equals(user.id);
	}

	public boolean matchPassword(String password) {
		return this.information.getPassword().equals(password);
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return this.information.getUsername();
	}

	public String getPassword() {
		return this.information.getPassword();
	}

	public String getEmail() {
		return this.information.getEmail();
	}

	public String getGithubUrl() {
		return this.information.getGithubUrl();
	}

	public String getFacebookUrl() {
		return this.information.getFacebookUrl();
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", information=" + information +
				'}';
	}
}
