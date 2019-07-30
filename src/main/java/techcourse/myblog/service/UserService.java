package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(userDto.toEntity());
        return new UserDto(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Transactional
    public void updateUser(String userEmail, UserDto userDto) {
        User user = userRepository.findByEmail(Email.of(userEmail)).orElseThrow(UserException::new);
        user.updateNameAndEmail(userDto.getName(), userDto.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(Email.of(userEmail)).orElseThrow(UserException::new);
    }

    @Transactional
    public void deleteUser(String userEmail) {
        userRepository.delete(getUserByEmail(userEmail));
    }
}
