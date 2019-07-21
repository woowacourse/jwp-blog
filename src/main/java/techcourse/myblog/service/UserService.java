package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.web.dto.LoginRequestDto;
import techcourse.myblog.web.dto.UserRequestDto;
import techcourse.myblog.web.dto.UserUpdateRequestDto;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long register(UserRequestDto request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 같지 않습니다.");
        }
        User newUser = User.of(request.getName(), request.getEmail(), request.getPassword(),
            email -> userRepository.findByEmail(email).isPresent());
        userRepository.save(newUser);
        return newUser.getId();
    }

    public User authenticate(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(UserAuthenticateException::new);

        if (!user.authenticate(request.getEmail(), request.getPassword())) {
            throw new UserAuthenticateException();
        }

        return user;
    }

    public User update(Long id, UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserUpdateException("사용자를 찾을 수 없습니다"));
        user.update(User.of(request.getName(), user.getEmail(), user.getPassword(),
            email -> userRepository.findByEmail(email).isPresent()));
        userRepository.save(user);
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
