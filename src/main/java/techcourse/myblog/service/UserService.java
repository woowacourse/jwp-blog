package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.converter.ToUser;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.exception.SignUpException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private static final String REGISTERED_EMAIL = "이미 등록된 이메일 입니다.";
    private static final String NOT_FOUND_EMAIL = "이메일을 찾을 수 없습니다.";
    private final UserRepository userRepository;
    private final ToUser toUser;

    public UserService(UserRepository userRepository, ToUser toUser) {
        this.userRepository = userRepository;
        this.toUser = toUser;
    }

    @Transactional(rollbackFor = SignUpException.class)
    public User addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        return userRepository.save(toUser.convert(userRequestDto));
    }

    private void checkRegisteredEmail(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new SignUpException(REGISTERED_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = UserException.class)
    public User updateUser(UserRequestDto userRequestDto, User origin) {
        User user = getUserByEmail(origin);
        user.updateNameAndEmail(userRequestDto.getName(), userRequestDto.getEmail());
        return user;
    }

    private User getUserByEmail(User user) {
        return userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserException(NOT_FOUND_EMAIL));
    }

    @Transactional(rollbackFor = UserException.class)
    public void deleteUser(User user) {
        userRepository.delete(getUserByEmail(user));
    }
}