package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserAssembler;
import techcourse.myblog.dto.user.SignUpRequest;
import techcourse.myblog.dto.user.UpdateUserRequest;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.user.SignUpException;
import techcourse.myblog.exception.user.UserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String REGISTERED_EMAIL = "이미 등록된 이메일 입니다.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse addUser(SignUpRequest signUpRequestDto) {
        checkRegisteredEmail(signUpRequestDto);
        User user = userRepository.save(UserAssembler.toEntity(signUpRequestDto));

        return UserAssembler.toDto(user);
    }

    private void checkRegisteredEmail(SignUpRequest signUpRequestDto) {
        if (userRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new SignUpException(REGISTERED_EMAIL);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(UpdateUserRequest updateUserRequestDto, UserResponse origin) {
        User user = getUserByEmail(origin);
        user.updateNameAndEmail(UserAssembler.toEntity(updateUserRequestDto));
        return UserAssembler.toDto(user);
    }

    User getUserByEmail(UserResponse userResponse) {
        return userRepository.findByEmail(userResponse.getEmail()).orElseThrow(UserException::new);
    }

    @Transactional
    public void deleteUser(UserResponse userResponse) {
        User user = getUserByEmail(userResponse);
        userRepository.delete(user);
    }
}