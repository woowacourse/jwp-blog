package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class UserService {
    public static final String USER_SESSION_KEY = "user";
    final private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(final UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException("이메일이 중복됩니다.");
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        User retrieveUser = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return retrieveUser;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }

    public User update(final String email, final String name) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(email))
            .orElseThrow(UserNotFoundException::new);
        retrieveUser.update(Objects.requireNonNull(name));
        return retrieveUser;
    }

    public void delete(final Long id) {
        User retrieveUser = userRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(UserNotFoundException::new);
        userRepository.delete(retrieveUser);
    }
}
