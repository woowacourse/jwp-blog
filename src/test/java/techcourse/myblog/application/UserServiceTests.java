package techcourse.myblog.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.EditException;
import techcourse.myblog.application.exception.LoginException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.support.encrytor.EncryptHelper;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    private static final Long USER1_ID = 1L;
    private static final Long USER2_ID = 2L;
    private static final String CHANGED_NAME = "cmo";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EncryptHelper encryptHelper;

    private static final User USER_1 = new User("amo", "amo@woowahan.com", "PassWord123!");
    private static final User USER_2 = new User("bmo", "bmo@woowahan.com", "PassWord12!");

    private static final UserRequest USER_REQUEST_1 = new UserRequest("amo", "amo@woowahan.com", "PassWord123!");
    private static final UserRequest USER_REQUEST_2 = new UserRequest("bmo", "bmo@woowahan.com", "PassWord12!");

    private static final UserResponse USER_RESPONSE_1 = new UserResponse(USER1_ID, "amo", "amo@woowahan.com");
    private static final UserResponse USER_RESPONSE_2 = new UserResponse(USER2_ID, "bmo", "bmo@woowahan.com");

    private static final LoginRequest LOGIN_REQUEST_1 = new LoginRequest("amo@woowahan.com", "PassWord123!");
    private static final LoginRequest LOGIN_REQUEST_2 = new LoginRequest("bmo@woowahan.com", "PassWord12!");

    @Test
    void 정상_유저_조회_저장() {
        given(encryptHelper.encrypt(USER_REQUEST_1.getPassword()))
                .willReturn(USER_REQUEST_1.getPassword());
        given(modelMapper.map(USER_REQUEST_1, User.class)).willReturn(USER_1);
        userService.save(USER_REQUEST_1);

        USER_1.setId(USER1_ID);
        verify(userRepository).save(USER_1);
        verify(userRepository, never()).save(USER_2);
    }

    @Test
    void 모든_유저_정상_조회() {
        given(userRepository.findAll()).willReturn(Arrays.asList(USER_1, USER_2));
        given(modelMapper.map(USER_1, UserResponse.class)).willReturn(USER_RESPONSE_1);
        given(modelMapper.map(USER_2, UserResponse.class)).willReturn(USER_RESPONSE_2);

        userService.findAll();

        verify(userRepository).findAll();
    }

    @Test
    void 로그인_정상_성공() {
        given(userRepository.findUserByEmail(LOGIN_REQUEST_1.getEmail())).willReturn(Optional.of(USER_1));
        given(encryptHelper.isMatch(LOGIN_REQUEST_1.getPassword(), USER_1.getPassword())).willReturn(true);
        given(modelMapper.map(USER_1, UserResponse.class)).willReturn(USER_RESPONSE_1);

        userService.login(LOGIN_REQUEST_1);

        verify(userRepository).findUserByEmail(LOGIN_REQUEST_1.getEmail());
    }

    @Test
    void 일치하지_않는_이메일_로그인_비정상_오류() {
        assertThrows(LoginException.class, () -> userService.login(LOGIN_REQUEST_1));
    }

    @Test
    void 일치하지_않는_비밀번호_로그인_비정상_오류() {
        given(userRepository.findUserByEmail(LOGIN_REQUEST_1.getEmail())).willReturn(Optional.of(USER_1));
        given(encryptHelper.isMatch(LOGIN_REQUEST_1.getPassword(), USER_2.getPassword())).willReturn(false);

        assertThrows(LoginException.class, () -> userService.login(LOGIN_REQUEST_1));
    }

    @Test
    void 유저_이름_정상_수정() {
        given(userRepository.findById(USER1_ID)).willReturn(Optional.of(USER_1));
        given(modelMapper.map(USER_1, UserResponse.class)).willReturn(USER_RESPONSE_1);

        userService.modify(USER1_ID, CHANGED_NAME);

        verify(userRepository).findById(USER1_ID);
    }

    @Test
    void 존재하지_않는_유저_이름_수정_오류() {
        assertThrows(NoUserException.class, () -> userService.modify(USER1_ID, CHANGED_NAME));
    }

    @Test
    void 정상적이지_않은_이름으로_수정_오류() {
        given(userRepository.findById(USER1_ID)).willReturn(Optional.of(USER_1));
        given(modelMapper.map(USER_1, UserResponse.class)).willReturn(USER_RESPONSE_1);

        String abnormalName = "sddsfsdffsdfdsf";
        assertThrows(EditException.class, () -> userService.modify(USER1_ID, abnormalName));
    }

    @Test
    void delete() {
        userService.remove(USER_1.getId());
        verify(userRepository).deleteById(USER_1.getId());
    }
}