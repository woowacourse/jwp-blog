package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.NoSuchUserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static final long DEFAULT_USER_ID = 0L;
    private static final String NAME = "코니코니";
    private static final String EMAIL = "cony@naver.com";
    private static final String PASSWORD = "@Password1234";

    @Autowired
    private UserService userService;

//    @MockBean(name = "userRepository") // 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태
//    private UserRepository userRepository;

    @Test
    public void 중복된_이메일을_등록하는_경우_예외처리() {
        enrollUser();
        User user = new User(DEFAULT_USER_ID, NAME, EMAIL, PASSWORD);

        assertThrows(DuplicateUserException.class, () -> userService.createUser(UserAssembler.writeDto(user)));
    }

    @Test
    public void 등록된_사용자가_로그인을_하는_경우_테스트() {
        enrollUser();

        User user = userService.findUserByEmailAndPassword(EMAIL);

        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void 등록되지_않은_사용자가_로그인을_하는_경우_예외처리() {
        assertThrows(NoSuchUserException.class, () -> userService.findUserByEmailAndPassword(EMAIL));
    }

    @Test
    public void 존재하지_않는_회원정보를_수정하는_경우_예외처리() {
        UserDto userDto = new UserDto();
        userDto.setName("");
        assertThrows(NoSuchUserException.class, () -> userService.updateUser(userDto));
    }

    private void enrollUser() {
        User user = new User(DEFAULT_USER_ID, NAME, EMAIL, PASSWORD);
        userService.createUser(UserAssembler.writeDto(user));
    }
}