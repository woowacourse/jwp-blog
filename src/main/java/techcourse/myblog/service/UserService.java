package techcourse.myblog.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        log.debug("UserService.save() : {}", userDto);
        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);
        return new UserDto(user.getEmail(), user.getName(), user.getPassword());
    }

    private void emailDuplicateValidate(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException();
        }
    }

    public boolean checkPassword(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());
        if (maybeUser.isPresent() && maybeUser.get().isSamePassword(userDto.getPassword())) {
            return true;
        }
        throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }

    public Optional<UserDto> getUserDtoByEmail(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            return Optional.of(new UserDto(user.getEmail(), user.getName(), user.getPassword()));
        }
        return Optional.empty();
    }

    public String updateUserName(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        String updatedName = user.updateName(userDto);
        userRepository.save(user);
        log.debug("updateUserName : {} {}", updatedName, userDto);
        return user.getName();
    }

    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        userRepository.delete(user);
        log.debug("delete User : {}", user);
    }

    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
