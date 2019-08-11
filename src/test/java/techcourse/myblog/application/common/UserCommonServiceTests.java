package techcourse.myblog.application.common;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.UserAssembler;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.application.UserReadService;
import techcourse.myblog.application.UserWriteService;

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
        user = UserAssembler.toEntity(SIGN_UP_USER_DTO);
    }

    protected void compareUser(User user1, UserResponseDto user2) {
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
    }
}
