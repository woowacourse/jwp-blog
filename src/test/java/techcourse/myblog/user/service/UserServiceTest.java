package techcourse.myblog.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.dto.UserUpdateDto;
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
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .email(UserDataForTest.USER_EMAIL)
                .password(UserDataForTest.USER_PASSWORD)
                .name(UserDataForTest.USER_NAME)
                .build();

        userService.save(modelMapper.map(user, UserCreateDto.class));
    }

    @Test
    void 회원정보_등록시_예외처리() {
        UserCreateDto duplicatedUser = modelMapper.map(user, UserCreateDto.class);
        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }

    @Test
    void 회원정보_전체_조회_테스트() {
        List<UserResponseDto> users = userService.findAll();
        assertThat(users).isEqualTo(Arrays.asList(modelMapper.map(user, UserResponseDto.class)));
    }

    @Test
    void 회원정보_단건_성공_조회_테스트() {
        assertThat(userService.findById(userId)).isEqualTo(modelMapper.map(user, UserResponseDto.class));
    }

    @Test
    void 회원정보_단건_실패_조회_테스트() {
        long notExistedUserId = userId - 1;
        assertThrows(NotFoundUserException.class, () -> userService.findById(notExistedUserId));
    }

    @Test
    void 로그인_성공_테스트() {
        UserLoginDto login = modelMapper.map(user, UserLoginDto.class);
        assertDoesNotThrow(() -> userService.login(login));
    }

    @Test
    void 로그인_시_아이디_불일치_예외처리() {
        UserLoginDto wrongIdLogin = new UserLoginDto();
        wrongIdLogin.setEmail("wrong");
        wrongIdLogin.setPassword(UserDataForTest.USER_PASSWORD);
        assertThrows(NotFoundUserException.class, () -> userService.login(wrongIdLogin));
    }

    @Test
    void 로그인_시_비밀번호_불일치_예외처리() {
        UserLoginDto wrongPasswordLogin = new UserLoginDto();
        wrongPasswordLogin.setEmail(UserDataForTest.USER_EMAIL);
        wrongPasswordLogin.setPassword("wrong");
        assertThrows(NotMatchPasswordException.class, () -> userService.login(wrongPasswordLogin));
    }

    @Test
    void 회원_정보_수정_테스트() {
        User updatedUser = User.builder()
                .id(userId)
                .email(UserDataForTest.USER_EMAIL)
                .password(UserDataForTest.USER_PASSWORD)
                .name("updated")
                .build();
        UserResponseDto result = userService.update(userId, modelMapper.map(updatedUser, UserUpdateDto.class));
        assertThat(result).isEqualTo(modelMapper.map(updatedUser, UserResponseDto.class));
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(userId++);
    }
}