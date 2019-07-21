package techcourse.myblog.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.exception.UserCreationException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class User {
	private static final Logger log = LoggerFactory.getLogger(User.class);

	private static final int NAME_MIN_LENGTH = 2;
	private static final int NAME_MAX_LENGTH = 8;
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣|a-zA-Z]+$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9@#$%^&+=]+$");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;

	public User() {
	}

	public User(final long id, final String name, final String email, final String password) {
		this(name, email, password);
		this.id = id;
	}

	public User(final String name, final String email, final String password) {
		validate(name, password);
		this.name = name;
		this.email = email;
		this.password = password;
	}

	private void validate(final String name, final String password) {
		validateName(name);
		validatePassword(password);
	}

	private void validateName(final String name) {
		log.debug("name in User.class : {}", name);
		if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
			throw new UserCreationException("이름은 " + NAME_MIN_LENGTH +
					"이상 " + NAME_MAX_LENGTH + "이하로 작성하세요");
		}

		if (!NAME_PATTERN.matcher(name).find()) {
			throw new UserCreationException("이름은 한글 또는 영어이름으로 입력하세요");
		}
	}

	private void validatePassword(final String password) {
		log.debug("password in User.class : {}", password);
		if (password.length() < PASSWORD_MIN_LENGTH) {
			throw new UserCreationException("이름은 " + NAME_MIN_LENGTH +
					"이상 " + NAME_MAX_LENGTH + "이하로 작성하세요");
		}

		if (!PASSWORD_PATTERN.matcher(password).find()) {
			throw new UserCreationException("비밀번호는 소대문자 영어 + 숫자 조합입니다");
		}
	}

	public boolean isSamePassword(String password) {
		return this.password.equals(password);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return id.equals(user.id) &&
				name.equals(user.name) &&
				email.equals(user.email) &&
				password.equals(user.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, email, password);
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
