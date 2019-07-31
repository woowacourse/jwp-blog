package techcourse.myblog.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.domain.vo.Email;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.dto.UserUpdateDto;
import techcourse.myblog.user.exception.DuplicatedUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.NotMatchPasswordException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void save(UserCreateDto userCreateDto) {
        User newUser = userCreateDto.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        userRepository.save(newUser);
    }

    public List<UserResponseDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    public UserResponseDto findById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(Email.of(userLoginDto.getEmail())).orElseThrow(NotFoundUserException::new);

        if (!user.checkPassword(userLoginDto.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto update(long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.update(userUpdateDto.getName());
        return modelMapper.map(user, UserResponseDto.class);
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }
}