package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.user.*;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SnsInfoRepository snsInfoRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private SnsInfoService snsInfoService;

    long userId = 1;
    String email = "test";
    String password = "test";
    String name = "test";

    UserDto userDto = SignUpDto.builder().name(name)
            .email(email).password(password).build();

    @Test
    void 유저_생성() {
        when(userRepository.save(userDto.toEntity())).thenReturn(userDto.toEntity());
        assertThat(userService.create(userDto)).isEqualToComparingFieldByField(userDto);
    }

    @Test
    void 유저_조회() {
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(userDto.toEntity()));
        assertThat(userService.findByUserDto(userDto)).isEqualTo(userDto);
    }

    @Test
    void 유저_전체_조회() {
        UserDto userDto1 = SignUpDto.builder().name(name)
                .email(email).password(password).build();

        UserDto userDto2 = SignUpDto.builder().name(name)
                .email(email).password(password).build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(userDto1.toEntity(), userDto2.toEntity()));
        assertThat(userService.readAll()).isEqualTo(Arrays.asList(userDto1, userDto2));
    }

    @Test
    void 유저_업데이트() {
        UserInfoDto userInfoDto = UserInfoDto.builder().id(2).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDto.toEntity()));
        doNothing().when(snsInfoRepository).deleteById(userId);

        userService.update(userDto, userInfoDto);
        verify(userRepository, times(2)).findById(userId);
    }

    @Test
    void 유저_삭제() {
        doNothing().when(snsInfoRepository).deleteById(userId);
        doNothing().when(snsInfoRepository).deleteById(userId);
        userService.deleteById(userDto);
        verify(userRepository, times(1)).deleteById(userId);
        verify(snsInfoService, times(1)).deleteByUserId(userId);
    }

}