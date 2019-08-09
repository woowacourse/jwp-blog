package techcourse.myblog.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.exception.EmailDuplicatedException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long save(UserDto userDto) {
        User newUser = new User(userDto.getUserName(), userDto.getEmail(), userDto.getPassword());

        try {
            userRepository.save(newUser);
            return newUser.getId();
        } catch (DataIntegrityViolationException e) {
            throw new EmailDuplicatedException("이미 사용중인 이메일입니다.");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(UserDto userDto) {
        User oldUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("유저 정보가 없습니다."));
        User updatedUser = new User(userDto.getUserName(), userDto.getEmail(), userDto.getPassword());
        return oldUser.update(updatedUser);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}