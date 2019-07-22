package techcourse.myblog.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        log.debug("UserService.save() : " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .build();
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

    @Transactional
    public String updateUserName(UserDto userDto, String name) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        user.updateName(name);
        userRepository.save(user);
        return user.getName();
    }

    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        userRepository.delete(user);
        log.debug("delete User : " + user.toString());
    }
}
