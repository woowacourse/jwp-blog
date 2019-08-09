package techcourse.myblog.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;

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

    public UserResponse save(final UserRequest userRequest) {
        User user = convertToEntity(Objects.requireNonNull(userRequest));
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException();
        }
        return convertToDto(userRepository.save(user));
    }

    public UserResponse findById(final Long id) {
        User retrieveUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return convertToDto(retrieveUser);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserAssembler::convertToDto)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Transactional
    public UserResponse update(final UserResponse accessUser, final UserRequest userInfo) {
        User retrieveUser = userRepository.findById(Objects.requireNonNull(accessUser.getId()))
                .orElseThrow(UserNotFoundException::new);
        User user = convertToEntity(userInfo);
        retrieveUser.update(user);
        return convertToDto(retrieveUser);
    }

    public void delete(final UserResponse user) {
        User retrieveUser = userRepository.findById(Objects.requireNonNull(user).getId())
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(retrieveUser);
    }
}
