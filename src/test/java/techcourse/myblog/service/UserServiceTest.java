package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.SnsInfo;
import techcourse.myblog.domain.SnsInfoRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserMypageRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.exception.UserNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserService.class, UserRepository.class, SnsInfoRepository.class})
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "snsInfoRepository")
    private SnsInfoRepository snsInfoRepository;

    @Test
    void save_() {
        // given
        UserSaveRequestDto dto = new UserSaveRequestDto(TestUser.VALID_NAME, TestUser.VALID_EMAIL, TestUser.VALID_PASSWORD);
        User userForSaving = dto.toEntity();

        Long expectedId = 10l;
        User savedUser = dto.toEntityWithId(expectedId);
        given(userRepository.save(any())).willReturn(savedUser);

        assertThat(userService.save(dto)).isEqualTo(savedUser);
    }

    @Test
    void findAll_3명() {
        // given
        int numUser = 3;
        List<User> expectedUsers = TestUser.createValidUsers(numUser);
        given(userRepository.findAll()).willReturn(expectedUsers);

        List<UserResponseDto> foundUsers = userService.findAll();

        expectedUsers.stream()
                .map(UserResponseDto::from)
                .forEach(user -> assertThat(foundUsers.contains(user)).isTrue());
    }

    @Test
    void findById_존재하는_id() {
        User expectedUser = TestUser.createValidUsers(1).get(0);
        Long id = expectedUser.getId();
        given(userRepository.findById(id)).willReturn(Optional.of(expectedUser));

        assertThat(userService.findById(id)).isEqualTo(Optional.of(UserResponseDto.from(expectedUser)));
    }

    @Test
    void findById_존재하지않는_id() {
        User expectedUser = TestUser.createValidUsers(1).get(0);
        Long id = expectedUser.getId();
        given(userRepository.findById(id)).willReturn(Optional.of(expectedUser));

        Long notExistId = id + 1;
        assertThat(userService.findById(notExistId)).isEqualTo(Optional.empty());
    }


    @Test
    void update_존재하지않는_id() {
        User dirtyUser = TestUser.createValidUsers(1).get(0);
        Long id = dirtyUser.getId();
        UserMypageRequestDto dto = new UserMypageRequestDto(null, null, null, null, null);

        Long notExistId = id + 1;

        assertThrows(UserNotFoundException.class, () -> userService.update(notExistId, dto));
    }

    // 무엇을 해야할까?
    // userRepository 에서 읽어오기
    // 기존의 존재하던 userRepository 에 변경된 값이 저장되어야 함
    @Test
    void update_존재하는_id() {
        User dirtyUser = TestUser.createValidUsers(1).get(0);
        Long id = dirtyUser.getId();
        String newName = "new name";
        String githubEmail = "github email";
        dirtyUser.addSnsInfo(snsBuiltWithEmail(githubEmail));
        dirtyUser.addSnsInfo(snsBuiltWithEmail("old facebook email"));
        given(userRepository.findById(id)).willReturn(Optional.of(dirtyUser));

        String updatedFacebookEmail = "new facebook email";
        List<SnsInfo> snsInfos = Arrays.asList(
                snsBuiltWithEmail(githubEmail),
                snsBuiltWithEmail(updatedFacebookEmail));
        UserMypageRequestDto updatedDto = new UserMypageRequestDto(null, newName, null, githubEmail, updatedFacebookEmail);
        User updatedUser = dirtyUser.replace(newName, snsInfos);


        userService.update(id, updatedDto);


        // userRepository 의 save 를 호출했는지 검사
        InOrder inOrder = inOrder(snsInfoRepository, userRepository);
        inOrder.verify(snsInfoRepository).deleteByUserId(id);
        inOrder.verify(userRepository).save(updatedUser);
    }

    @Test
    void deleteById() {
        Long id = 10l;

        userService.deleteById(id);

        verify(userRepository).deleteById(id);
    }

    private SnsInfo snsBuiltWithEmail(String email) {
        return SnsInfo.builder().email(email).build();
    }
}