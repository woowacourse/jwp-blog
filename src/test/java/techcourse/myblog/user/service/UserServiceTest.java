package techcourse.myblog.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.dto.UserDto;
import techcourse.myblog.user.exception.DuplicatedUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.NotMatchPasswordException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static long userId = 1;

    @Autowired
    private techcourse.myblog.user.service.UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .email("email")
                .password("password")
                .name("name")
                .build();

        userService.save(modelMapper.map(user, UserDto.Creation.class));
    }

    @Test
    void 회원정보_등록시_예외처리() {
        UserDto.Creation duplicatedUser = modelMapper.map(user, UserDto.Creation.class);
        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }

    @Test
    void 회원정보_전체_조회_테스트() {
        List<UserDto.Response> users = userService.findAll();
        assertThat(users).isEqualTo(Arrays.asList(modelMapper.map(user, UserDto.Response.class)));
    }

    @Test
    void 회원정보_단건_성공_조회_테스트() {
        assertThat(userService.findById(userId)).isEqualTo(modelMapper.map(user, UserDto.Response.class));
    }

    @Test
    void 회원정보_단건_실패_조회_테스트() {
        long notExistedUserId = userId - 1;
        assertThrows(NotFoundUserException.class, () -> userService.findById(notExistedUserId));
    }

    @Test
    void 로그인_성공_테스트() {
        UserDto.Login login = modelMapper.map(user, UserDto.Login.class);
        assertDoesNotThrow(() -> userService.login(login));
    }

    @Test
    void 로그인_시_아이디_불일치_예외처리() {
        UserDto.Login wrongIdLogin = new UserDto.Login();
        wrongIdLogin.setEmail("wrong");
        wrongIdLogin.setPassword("password");
        assertThrows(NotFoundUserException.class, () -> userService.login(wrongIdLogin));
    }

    @Test
    void 로그인_시_비밀번호_불일치_예외처리() {
        UserDto.Login wrongPasswordLogin = new UserDto.Login();
        wrongPasswordLogin.setEmail("email");
        wrongPasswordLogin.setPassword("wrong");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(wrongPasswordLogin));
    }

    @Test
    void 회원_정보_수정_테스트() {
        User updatedUser = User.builder()
                .id(userId)
                .email("email")
                .password("password")
                .name("updatedName")
                .build();
        UserDto.Response result = userService.update(userId, modelMapper.map(updatedUser, UserDto.Updation.class));
        assertThat(result).isEqualTo(modelMapper.map(updatedUser, UserDto.Response.class));
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(userId++);
    }
}