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

    public User save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        userDto.setPassword(userDto.getPassword());
        log.info("UserService.save() : " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        userRepository.save(user);
        return user;
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
            return Optional.of(new UserDto(user.getEmail(),user.getName(),user.getPassword()));
        }
        return Optional.empty();
    }

    public String updateUserName(UserDto userDto, String name) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        user.setName(name);
        userRepository.save(user);
        log.info("updateUserName에서 의 name " + name);
        return user.getName();
    }

    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        userRepository.delete(user);
        log.info("delete User : " + user.toString());
    }
}
