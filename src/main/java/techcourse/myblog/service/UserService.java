package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.exception.NoUserException;
import techcourse.myblog.exception.SignUpFailException;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {
    private static final String NO_USER_MESSAGE = "존재하지 않는 유저입니다.";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(UserSignUpRequestDto userSignUpRequestDto) {
        if (userRepository.existsByEmail(userSignUpRequestDto.getEmail())) {
            throw new SignUpFailException("중복된 이메일 입니다.");
        }
        User user = new User(userSignUpRequestDto);
        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(UserUpdateRequestDto userUpdateRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoUserException(NO_USER_MESSAGE));
        user.update(userUpdateRequestDto);
        return user;
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoUserException(NO_USER_MESSAGE));
        userRepository.delete(user);
    }
}
