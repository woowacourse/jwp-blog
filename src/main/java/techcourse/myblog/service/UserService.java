package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.repository.UserRepository;
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
        User user = getUserByEmail(origin);
        user.updateNameAndEmail(userRequestDto);
        return UserConverter.convert(user);
    }

    private User getUserByEmail(UserResponseDto userResponseDto) {
        return userRepository.findByEmail(userResponseDto.getEmail())
                .orElseThrow(() -> new NotFoundUserException());
    }

    @Transactional()
    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto));
    }
}