package techcourse.myblog.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static techcourse.myblog.data.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    private User user;
    private UserCreateDto userCreateDto;
    private UserLoginDto userLoginDto;
    private UserUpdateDto userUpdateDto;
    private UserResponseDto userResponseDto;


    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .name(USER_NAME)
                .build();

        userCreateDto = modelMapper.map(user, UserCreateDto.class);
        userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userLoginDto = modelMapper.map(user, UserLoginDto.class);
        userUpdateDto = modelMapper.map(user, UserUpdateDto.class);
    }

    @Test
    void 중복_이메일_체크() {
        when(userRepository.findByEmail(Email.of(USER_EMAIL))).thenReturn(Optional.of(user));

        assertThrows(DuplicatedUserException.class, () -> userService.save(userCreateDto));
    }

    @Test
    void 유저저장() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(userCreateDto.toUser())).thenReturn(user);

        assertThat(userService.save(userCreateDto).getName()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getName());
        assertThat(userService.save(userCreateDto).getEmail()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getEmail());
        assertThat(userService.save(userCreateDto).getId()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getId());
    }

    @Test
    void findAll() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        assertThat(userService.findAll().get(0).getName()).isEqualTo(users.get(0).getName().getName());
        assertThat(userService.findAll().get(0).getEmail()).isEqualTo(users.get(0).getEmail().getEmail());
        assertThat(userService.findAll().get(0).getId()).isEqualTo(users.get(0).getId());
    }

    @Test
    void 없는_아이디_검색() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void 있는_아이디_검색() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseDto userResponse = userService.findById(user.getId());
        assertThat(userResponse.getId()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getId());
        assertThat(userResponse.getName()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getName());
        assertThat(userResponse.getEmail()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getEmail());
    }

    @Test
    void 없는_이메일_로그인() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.login(userLoginDto));
    }

    @Test
    void 있는_이메일_패스워드불일치_로그인() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        userLoginDto.setPassword("WrongPassword!");

        assertThrows(NotMatchPasswordException.class, () -> userService.login(userLoginDto));
    }

    @Test
    void 로그인_성공() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(userService.login(userLoginDto).getName()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getName());
        assertThat(userService.login(userLoginDto).getEmail()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getEmail());
        assertThat(userService.login(userLoginDto).getId()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getId());
    }

    @Test
    void 없는아이디_업데이트시도() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.update(user.getId(), userUpdateDto));
    }

    @Test
    void 유저_업데이트() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseDto userResponse = userService.update(user.getId(), userUpdateDto);
        assertThat(userResponse.getId()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getId());
        assertThat(userResponse.getName()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getName());
        assertThat(userResponse.getEmail()).isEqualTo(modelMapper.map(user, UserResponseDto.class).getEmail());
    }

    @Test
    void deleteById() {
        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteById(user.getId());
    }
}