package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(final UserRequestDto.SignUpRequestDto signUpRequestDto) {
        return userRepository.save(signUpRequestDto.toUser());
    }

    @Transactional
    public User update(final UserRequestDto.UpdateRequestDto updateRequestDto) {
        User user = findByEmail(updateRequestDto.getEmail());
        return user.update(updateRequestDto.getName());
    }

    private User findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public User findById(final long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean exitsByEmail(final UserRequestDto.SignUpRequestDto signUpRequestDto) {
        try {
            return userRepository.existsByEmail(signUpRequestDto.getEmail());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void deleteByEmail(final String email) {
        User user = findByEmail(email);
        userRepository.deleteById(user.getId());
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
