package techcourse.myblog.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.domain.vo.Email;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.dto.UserUpdateDto;
import techcourse.myblog.user.exception.DuplicatedUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.NotMatchPasswordException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private static final long USER_ID = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(USER_ID)
                .email(UserDataForTest.USER_EMAIL)
                .password(UserDataForTest.USER_PASSWORD)
                .name(UserDataForTest.USER_NAME)
                .build();
    }

    @Test
    void 회원정보_등록_테스트() {
        UserCreateDto userCreateDto = modelMapper.map(user, UserCreateDto.class);

        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.empty());
        given(userRepository.save(userCreateDto.toUser())).willReturn(user);

        assertThat(userService.save(userCreateDto))
                .isEqualTo(modelMapper.map(user, UserResponseDto.class));
    }

    @Test
    void 회원정보_등록시_예외처리() {
        UserCreateDto duplicatedUser = modelMapper.map(user, UserCreateDto.class);

        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(user));

        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }

    @Test
    void 회원정보_전체_조회_테스트() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user));

        assertThat(userService.findAll()).isEqualTo(Arrays.asList(modelMapper.map(user, UserResponseDto.class)));
    }

    @Test
    void 회원정보_단건_성공_조회_테스트() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

        assertThat(userService.find(USER_ID)).isEqualTo(modelMapper.map(user, UserResponseDto.class));
    }

    @Test
    void 회원정보_단건_실패_조회_테스트() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.find(USER_ID));
    }

    @Test
    void 로그인_성공_테스트() {
        UserLoginDto login = modelMapper.map(user, UserLoginDto.class);

        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.login(login));
    }

    @Test
    void 로그인_시_아이디_불일치_예외처리() {
        UserLoginDto wrongIdLogin = modelMapper.map(user, UserLoginDto.class);

        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.login(wrongIdLogin));
    }

    @Test
    void 로그인_시_비밀번호_불일치_예외처리() {
        UserLoginDto wrongPasswordLogin = modelMapper.map(user, UserLoginDto.class);
        wrongPasswordLogin.setPassword("wrong1234!!");

        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(user));

        assertThrows(NotMatchPasswordException.class, () -> userService.login(wrongPasswordLogin));
    }

    @Test
    void 회원_정보_수정_테스트() {
        User updatedUser = User.builder()
                .id(USER_ID)
                .email(UserDataForTest.USER_EMAIL)
                .password(UserDataForTest.USER_PASSWORD)
                .name(UserDataForTest.UPDATED_USER_NAME)
                .build();

        UserUpdateDto userUpdateDto = modelMapper.map(updatedUser, UserUpdateDto.class);

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

        assertThat(userService.update(USER_ID, userUpdateDto))
                .isEqualTo(modelMapper.map(updatedUser, UserResponseDto.class));
    }

    @Test
    void 회원_정보_수정_시_없는_회원일_시_예외처리() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();

        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.update(USER_ID, userUpdateDto));
    }

    @Test
    void 회원_정보_삭제_테스트() {
        willDoNothing().given(userRepository).deleteById(USER_ID);

        assertDoesNotThrow(() -> userService.deleteById(USER_ID));
    }

    @Test
    void 회원_정보_삭제_시_없는_회원일_경우_예외처리() {
        willThrow(EmptyResultDataAccessException.class).given(userRepository).deleteById(USER_ID);

        assertThrows(NotFoundUserException.class, () -> userService.deleteById(USER_ID));
    }
}