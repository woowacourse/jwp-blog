package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.dto.UserResponseDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;
import techcourse.myblog.service.exception.UserNotFoundException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto update(String email, UserRequestDto userRequestDto) {
        log.info("");

        User user = userRepository.findByEmail(email).orElseThrow(ArticleNotFoundException::new);
        user.update(userRequestDto);

        return UserResponseDto.from(user);
    }

    public void delete(User user) {
        log.info("");

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

        return UserResponseDto.from(user);
    }

    public void save(UserRequestDto dto) {
        userRepository.save(dto.toUser());
    }

    @Transactional(readOnly = true)
    public boolean canSave(UserRequestDto dto) {
        return userRepository.findByEmail(dto.getEmail()).isPresent();
    }
}
