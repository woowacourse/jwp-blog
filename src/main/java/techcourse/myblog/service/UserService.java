package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.AuthenticationFailedException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public static final String NOT_EXIST_USER_MESSAGE = "존재하지 않는 user 입니다";
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";
    public static final String WRONG_PASSWORD_MESSAGE = "비밀번호를 확인해주세요";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedEmailException(DUPLICATED_USER_MESSAGE);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(UserDto userDto) {
        try {
            User user = findByEmail(userDto.getEmail());
            user.authenticate(userDto.getEmail(), userDto.getPassword());
            return user;
        } catch (UnfoundUserException | AuthenticationFailedException e) {
            throw new LoginFailedException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnfoundUserException(NOT_EXIST_USER_MESSAGE));
    }

    @Transactional
    public User modify(UserDto changedUser) {
        User user = findByEmail(changedUser.getEmail());
        user.modifyName(changedUser.getName());
        return user;
    }

    public void remove(User user) {
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
