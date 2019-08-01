package techcourse.myblog.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.exception.DuplicateUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.repository.UserRepository;
import techcourse.myblog.utils.converter.UserConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional()
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        User user = userRepository.save(UserConverter.convert(userRequestDto));

        return UserConverter.convert(user);
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

    @Transactional()
    public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto origin) {
        User user = getUserByEmail(origin.getEmail());
        user.updateNameAndEmail(userRequestDto);
        return UserConverter.convert(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    @Transactional()
    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto.getEmail()));
    }
}