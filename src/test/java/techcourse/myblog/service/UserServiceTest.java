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

    @InjectMocks
    private UserService userService;

    @Mock
    private SnsInfoService snsInfoService;

    long userId = 1;
    String email = "test321@test.com";
    String password = "test";
    String name = "test";

    UserDto userDto = SignUpDto.builder().name(name)
            .email(email).password(password).build();

    UserInfoDto userInfoDto = UserInfoDto.builder().id(1).name(name)
            .email(email).build();

    @Test
    void 유저_생성() {
        when(userRepository.save(userDto.toEntity())).thenReturn(userInfoDto.toEntity());
        assertThat(userService.create(userDto)).isEqualToComparingFieldByField(userInfoDto);
    }

    @Test
    void 유저_조회() {
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(userInfoDto.toEntity()));
        assertThat(userService.findByUserDto(userDto)).isEqualToComparingFieldByField(userInfoDto);
    }

    @Test
    void 유저_전체_조회() {
        UserDto userDto1 = UserInfoDto.builder().name(name)
                .email(email).build();

        UserDto userDto2 = UserInfoDto.builder().name(name)
                .email(email).build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(userDto1.toEntity(), userDto2.toEntity()));
        assertThat(userService.readAll()).isEqualTo(Arrays.asList(userDto1, userDto2));
    }

    @Test
    void 유저_업데이트() {
        UserInfoDto userInfoDto = UserInfoDto.builder().id(10).name(name)
                .email(email).build();

        when(userRepository.findById(10L)).thenReturn(Optional.of(userInfoDto.toEntity()));
        //doNothing().when(snsInfoService).deleteByUserId(userId);

        userService.update(userDto, userInfoDto);
        verify(userRepository, times(1)).findById(userId);
        verify(snsInfoService, times(1)).findByUserId(userId);
    }

    @Test
    void 유저_삭제() {
        userService.deleteById(userInfoDto);
        verify(userRepository, times(1)).deleteById(userId);
    }

}