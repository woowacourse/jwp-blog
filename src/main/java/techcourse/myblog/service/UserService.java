package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.web.UserDto;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(final UserDto.SignUpUserInfo signUpUserInfo) {
        return userRepository.save(signUpUserInfo.toUser());
    }

    public User save(final UserDto.UpdateInfo updateInfo) {
        User user = userRepository.findByEmail(updateInfo.getEmail())
                .orElseThrow(NoSuchElementException::new);
        user.setName(updateInfo.getName());
        return userRepository.save(user);
    }

    public User findById(final long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public User findByLoginInfo(final UserDto.LoginInfo loginInfo) {
        return userRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(NoSuchElementException::new);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public boolean exitsByEmail(final UserDto.SignUpUserInfo signUpUserInfo) {
        try {
            return userRepository.existsByEmail(signUpUserInfo.getEmail());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean canLogin(final UserDto.LoginInfo loginInfo) {
        try {
            User user = userRepository.findByEmail(loginInfo.getEmail()).orElseThrow(NoSuchElementException::new);
            return user.isSamePassword(loginInfo.getPassword());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void deleteByEmail(final String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        userRepository.deleteById(user.getId());
    }
}
