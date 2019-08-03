package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.SignUpRequest;
import techcourse.myblog.dto.UpdateUserRequest;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.exception.SignUpException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.utils.converter.DtoConverter;

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
        User user = userRepository.save(DtoConverter.convert(signUpRequestDto));

        return DtoConverter.convert(user);
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
        user.updateNameAndEmail(updateUserRequestDto.getName(), updateUserRequestDto.getEmail());
        return DtoConverter.convert(user);
    }

    User getUserByEmail(UserResponse userResponse) {
        return userRepository.findByEmail(userResponse.getEmail()).orElseThrow(UserException::new);
    }

    @Transactional
    public void deleteUser(UserResponse userResponse) {
        userRepository.delete(getUserByEmail(userResponse));
    }
}