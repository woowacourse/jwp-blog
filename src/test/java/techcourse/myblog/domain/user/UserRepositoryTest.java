package techcourse.myblog.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 생성, 수정일자 등록 확인")
    public void checkLocalDate() {

        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.save(new User("NAME", "user@test.test", "passWORD1!"));

        assertThat(user.getCreatedDateTime().isAfter(now)).isTrue();
        assertThat(user.getLastModifiedDateTime().isAfter(now)).isTrue();
    }
}