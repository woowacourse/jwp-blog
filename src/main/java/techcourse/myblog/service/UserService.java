package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.SignUpException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.converter.DtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String REGISTERED_EMAIL = "이미 등록된 이메일 입니다.";
    private static final String NOT_FOUND_EMAIL = "이메일을 찾을 수 없습니다.";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional()
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        User user = userRepository.save(DtoConverter.convert(userRequestDto));

        return DtoConverter.convert(user);
    }

    private void checkRegisteredEmail(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new SignUpException(REGISTERED_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Transactional()
    public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto origin) {
        User user = getUserByEmail(origin);
        user.updateNameAndEmail(userRequestDto);
        return DtoConverter.convert(user);
    }

    private User getUserByEmail(UserResponseDto userResponseDto) {
        return userRepository.findByEmail(userResponseDto.getEmail())
                .orElseThrow(() -> new UserException(NOT_FOUND_EMAIL));
    }

    @Transactional()
    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto));
    }
}