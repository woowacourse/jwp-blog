package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.converter.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(getUserByEmail(userRequestDto));

        return DtoConverter.convert(user);
    }

    private User getUserByEmail(UserRequestDto dto) {
        return userRepository.findByEmail(dto.getEmail()).orElseThrow(UserException::new);
    }

    public List<User> findAll() {
        return userRepository.findAll()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto origin) {
        User user = getUserByEmail(origin);
        user.updateNameAndEmail(userRequestDto.getName(), userRequestDto.getEmail());
        return DtoConverter.convert(user);
    }

    User getUserByEmail(UserResponseDto userResponseDto) {
        return userRepository.findByEmail(userResponseDto.getEmail()).orElseThrow(UserException::new);
    }

    @Transactional
    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto));
    }
}