package techcourse.myblog.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private SnsInfoRepository snsInfoRepository;

    @Transactional
    public User save(UserSaveRequestDto dto) {
        log.debug("called..!!");

        User user = userRepository.save(dto.toEntity());

        log.debug("user : " + user.toString());

        return user;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        log.debug("called..!!");

        return Lists.newArrayList(userRepository.findAll()).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id).map(UserResponseDto::from);
    }

    // TODO:
    // snsInfoRepository 에서 삭제하지 않고 관리할 수 있도록
    @Transactional
    public void update(long id, UserMypageRequestDto dto) {
        log.debug("dto: {}", dto);

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
                String.format("[%d] 인 id의 유저가 존재하지 않습니다.", id)
        ));

        User updatedUser = user.replace(dto.getName(), snsInfosFrom(user, dto));

        log.debug("updatedUser: {}", updatedUser);

        snsInfoRepository.deleteByUserId(id);
        log.debug("before userRepository.save");
        userRepository.save(updatedUser);
    }

    @Transactional
    public Optional<Long> findIdBy(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).map(user -> user.getId());
    }


    private List<SnsInfo> snsInfosFrom(User user, UserMypageRequestDto dto) {
        return Arrays.asList(
                SnsInfo.builder()
                        .user(user)
                        .email(dto.getSnsGithubEmail() == null ? "" : dto.getSnsGithubEmail() )
                        .build(),
                SnsInfo.builder()
                        .user(user)
                        .email(dto.getSnsFacebookEmail() == null ? "" : dto.getSnsFacebookEmail())
                        .build()
        );
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
