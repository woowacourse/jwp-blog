package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(userDto.toEntity());
        return new UserDto(user.getName(), user.getPassword(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateUser(User user, UserDto userDto) {
        user.updateNameAndEmail(userDto.getName(), userDto.getEmail());
        userRepository.saveAndFlush(user);
        return user;
    }

    @Transactional(readOnly = true)
    public UserDto findUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("존재하지 않는 회원입니다."));
        return new UserDto(user.getName(), user.getPassword(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmailEmail(userEmail).orElseThrow(UserException::new);
    }

    public void deleteUser(String userEmail) {
        userRepository.deleteById(getUserByEmail(userEmail).getId());
    }
}
