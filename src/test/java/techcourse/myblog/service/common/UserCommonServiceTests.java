package techcourse.myblog.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.utils.UserTestObjects.SIGN_UP_USER_DTO;

public class UserCommonServiceTests {
    @Mock
    protected UserRepository userRepository;

    protected UserWriteService userWriteService;
    protected UserReadService userReadService;
    protected User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userWriteService = new UserWriteService(userRepository);
        userReadService = new UserReadService(userRepository);
        user = SIGN_UP_USER_DTO.toUser();
    }

    protected void compareUser(User user1, User user2) {
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}
