package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.user.SnsInfoRepository;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.domain.user.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SnsInfoRepository snsInfoRepository;

    @Autowired
    private UserService userService;

    @Test
    void 유저_생성() {
        long userId = 1;
        String email = "test";
        String password = "test";
        String name = "test";
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        when(userRepository.save(userDto.toEntity())).thenReturn(userDto.toEntity());
        assertThat(userService.create(userDto)).isEqualToComparingFieldByField(userDto);
    }

    @Test
    void 유저_조회() {
        long userId = 1;
        String email = "test";
        String password = "test";
        String name = "test";
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(userDto.toEntity()));
        assertThat(userService.findUser(userDto)).isEqualTo(userDto);
    }

    @Test
    void 유저_전체_조회() {
        long userId = 1;
        String email = "test";
        String password = "test";
        String name = "test";
        UserDto userDto1 = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        UserDto userDto2 = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(userDto1.toEntity(), userDto2.toEntity()));
        assertThat(userService.readAll()).isEqualTo(Arrays.asList(userDto1, userDto2));
    }

    @Test
    void 유저_조회_비밀번호_없음() {
        long userId = 1;
        String email = "test";
        String password = "test";
        String name = "test";
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userDto.toEntity()));
        userDto.setPassword(null);
        assertThat(userService.readWithoutPasswordById(userId)).isEqualTo(userDto);
    }

    @Test
    void 유저_업데이트() {
        long userId = 1L;
        String email = "test";
        String password = "test";
        String name = "test";

        UserDto userDto = UserDto.builder()
                .id(userId)
                .name(name)
                .email(email)
                .password(password).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userDto.toEntity()));
        doNothing().when(snsInfoRepository).deleteById(userId);
        userService.updateUser(userId, userDto);
        verify(userRepository, times(2)).findById(userId);
        verify(snsInfoRepository, times(0)).deleteById(userId);
    }

    @Test
    void 유저_삭제() {
        long userId = 1L;

        doNothing().when(snsInfoRepository).deleteById(userId);
        doNothing().when(snsInfoRepository).deleteById(userId);
        userService.deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        verify(snsInfoRepository, times(1)).deleteByUserId(userId);
    }

}