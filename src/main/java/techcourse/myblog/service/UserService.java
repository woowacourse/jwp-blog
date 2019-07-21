package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(UserSignUpRequestDto userSignUpRequestDto) {
        if (userRepository.existsByEmail(userSignUpRequestDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }
        User user = new User(userSignUpRequestDto);
        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(UserUpdateRequestDto userUpdateRequestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        user.setUserName(userUpdateRequestDto.getUserName());
        return user;
    }

    public void delete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        userRepository.delete(user);
    }
}
