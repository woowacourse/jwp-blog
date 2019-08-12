package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.application.exception.LoginFailedException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmailAndPassword(UserRequestDto userRequestDto) {
        return userRepository.findByEmailAndPassword(userRequestDto.getEmail(), userRequestDto.getPassword())
                .orElseThrow(LoginFailedException::new);
    }

    public List<UserResponseDto> findAll() {
        return Collections.unmodifiableList(
                userRepository.findAll().stream()
                        .map(UserAssembler::buildUserResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
