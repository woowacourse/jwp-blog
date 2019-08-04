package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import techcourse.myblog.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	void findByEmail() {
		User user = userRepository.findByInformationEmail("tiber@naver.com").get();
		assertThat(user.getId()).isEqualTo(1L);
	}
}
