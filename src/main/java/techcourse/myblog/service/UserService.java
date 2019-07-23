package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.web.dto.LoginRequestDto;
import techcourse.myblog.web.dto.UserRequestDto;
import techcourse.myblog.web.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long register(UserRequestDto request) {
        User newUser = User.of(request.getName(), request.getEmail(), request.getPassword());
        userRepository.save(newUser);
        return newUser.getId();
    }

    @Transactional(readOnly = true)
    public User authenticate(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserAuthenticateException::new);

        if (!user.authenticate(request.getPassword())) {
            throw new UserAuthenticateException();
        }

        return user;
    }

    public User update(Long id, UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserUpdateException("사용자를 찾을 수 없습니다"));
        user.update(User.of(request.getName(), user.getEmail(), user.getPassword()));
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
