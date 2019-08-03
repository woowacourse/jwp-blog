package techcourse.myblog.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.List;

@Service
@Transactional
public class AccountService {
    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(UserDto userDto) {
        User user =userDto.toUser();
        userRepository.save(user);
    }

    public boolean isExistUser(UserDto userDto) {
        return userRepository.findByEmail(userDto.getEmail()).isPresent();
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(IllegalAccessError::new);
    }

    public User saveUser(UserDto userDto) {
        User user = userDto.toUser();
        userRepository.save(user);
        return user;
    }
}
