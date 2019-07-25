package techcourse.myblog.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.EmailNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static techcourse.myblog.service.user.UserAssembler.convertToDto;
import static techcourse.myblog.service.user.UserAssembler.convertToEntity;

@Service
public class UserService {
    public static final String USER_SESSION_KEY = "user";
    final private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto save(final UserRequestDto userRequestDto) {
        User user = convertToEntity(Objects.requireNonNull(userRequestDto));
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException("이메일이 중복됩니다.");
        }
        return convertToDto(userRepository.save(user));
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserAssembler::convertToDto)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public UserResponseDto update(final String email, final String name) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(email)).orElseThrow(EmailNotFoundException::new);
        retrieveUser.update(Objects.requireNonNull(name));
        return convertToDto(retrieveUser);
    }

    public void delete(final UserResponseDto user) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(user).getEmail())
                .orElseThrow(EmailNotFoundException::new);
        userRepository.delete(retrieveUser);
    }
}
