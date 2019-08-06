package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.exception.NotMatchUserException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends ServiceTest {
    @BeforeEach
    void setUp() {
        init();
    }

    @AfterEach
    void tearDown() {
        terminate();
    }

    @Test
    void 회원정보_등록시_이메일_중복() {
        UserDto.Create duplicatedUser = modelMapper.map(user, UserDto.Create.class);
        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }

    @Test
    void 회원정보_전체_조회_테스트() {
        List<UserDto.Response> users = userService.findAll();
        assertThat(users).isEqualTo(Arrays.asList(userDto, otherUserDto));
    }

    @Test
    void 회원정보_단건_성공_조회_테스트() {
        assertThat(userService.findById(userId)).isEqualTo(userDto);
    }

    @Test
    void 회원정보_단건_실패_조회_테스트() {
        long notExistedUserId = userId + 100;
        assertThrows(NotFoundUserException.class, () -> userService.findById(notExistedUserId));
    }

    @Test
    void 유저_확인_회원정보_단건_성공_조회_테스트() {
        assertThat(userService.findById(userDto, userId)).isEqualTo(userDto);
    }

    @Test
    void 유저_확인_회원정보_단건_실패_조회_테스트() {
        assertThrows(NotMatchUserException.class, () -> userService.findById(otherUserDto, userId));
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
        UserDto.Response result = userService.update(userDto, userId, modelMapper.map(updatedUser, UserDto.Update.class));
        assertThat(result).isEqualTo(modelMapper.map(updatedUser, UserDto.Response.class));
    }

    @Test
    void 회원_정보_실패_테스트() {
        User updatedUser = User.builder()
            .id(userId)
            .email("email")
            .password("password")
            .name("updatedName")
            .build();

        assertThrows(NotMatchUserException.class, () ->
            userService.update(otherUserDto, userId, modelMapper.map(updatedUser, UserDto.Update.class)));
    }

    @Test
    void 회원_탈퇴_실패_테스트() {
        assertThrows(NotMatchUserException.class, () ->
            userService.deleteById(otherUserDto, userId));
    }

}