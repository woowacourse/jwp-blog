package techcourse.myblog.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.dto.user.UserRequestDto;
import techcourse.myblog.dto.user.UserResponseDto;
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
    final private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto save(final UserRequestDto userRequestDto) {
        checkNull(userRequestDto);
        User user = convertToEntity(userRequestDto);
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException("이메일이 중복됩니다.");
        }
        return convertToDto(userRepository.save(user));
    }

    private void checkNull(final UserRequestDto userRequestDto) {
        if (Objects.isNull(userRequestDto)) {
            throw new NullPointerException();
        }
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserAssembler::convertToDto)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public UserResponseDto update(final String email, final String name) {
        checkNull(email, name);
        User retrieveUser = userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
        retrieveUser.update(name);
        return convertToDto(retrieveUser);
    }

    private void checkNull(final String email, final String name) {
        if (Objects.isNull(email) || Objects.isNull(name)) {
            throw new NullPointerException();
        }
    }

    public void delete(final UserResponseDto user) {
        User retrieveUser = userRepository.findByEmail(user.getEmail()).orElseThrow(EmailNotFoundException::new);
        userRepository.delete(retrieveUser);
    }
}
