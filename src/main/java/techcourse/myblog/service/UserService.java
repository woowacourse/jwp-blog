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
        //TODO 여기서 방어적 복사를 할 필요가 있을까?
        return new ArrayList<>(userRepository.findAll());
    }

    @Transactional
    public void updateUser(Email email, UserDto userDto) {
        User user = userRepository.findByEmail(email).orElseThrow(UserException::new);
        user.updateNameAndEmail(userDto.getName(), userDto.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(Email email) {
        return userRepository.findByEmail(email).orElseThrow(UserException::new);
    }

    @Transactional
    public void deleteUser(Email email) {
        userRepository.delete(getUserByEmail(email));
    }
}
