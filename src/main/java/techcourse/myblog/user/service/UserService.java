package techcourse.myblog.user.service;

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
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDto save(UserCreateDto userCreateDto) {
        User newUser = userCreateDto.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    public List<UserResponseDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    public UserResponseDto findById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(Email.of(userLoginDto.getEmail())).orElseThrow(() -> new NotFoundUserException(userLoginDto.getEmail()));

        if (!user.checkPassword(userLoginDto.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto update(long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        User updatedUser = user.update(userUpdateDto.getName());
        return modelMapper.map(updatedUser, UserResponseDto.class);
    }

    public boolean deleteById(long userId) {
        userRepository.deleteById(userId);
        return true;
    }
}