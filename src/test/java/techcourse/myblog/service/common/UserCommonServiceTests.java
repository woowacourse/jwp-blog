package techcourse.myblog.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class UserCommonServiceTests {
    @Mock
    protected UserRepository userRepository;

    protected UserWriteService userWriteService;
    protected UserReadService userReadService;

    protected User user = new User("name", "e@mail.com", "Passw0rd!");

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userReadService = new UserReadService(userRepository);
        userWriteService = new UserWriteService(userRepository, userReadService);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .willReturn(Optional.of(user));
        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);
    }

    protected void compareUser(User user1, User user2) {
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}
