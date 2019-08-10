package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.dto.UserResponseDto;
import techcourse.myblog.utils.converter.UserConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        User user = userRepository.save(UserConverter.toEntity(userRequestDto));

        return UserConverter.toResponseDto(user);
    }

    private void checkRegisteredEmail(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateUserException();
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto origin) {
        User user = getUserByEmail(origin.getEmail());
        user.updateNameAndEmail(userRequestDto);
        return UserConverter.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto.getEmail()));
    }
}