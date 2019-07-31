package techcourse.myblog.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.domain.vo.Email;
import techcourse.myblog.user.dto.UserDto;
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

    public User save(UserDto.Creation userDto) {
        User newUser = userDto.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        return userRepository.save(newUser);
    }

    public List<UserDto.Response> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.Response.class))
                .collect(Collectors.toList());
    }

    public UserDto.Response findById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        return modelMapper.map(user, UserDto.Response.class);
    }

    public UserDto.Response login(UserDto.Login userDto) {
        User user = userRepository.findByEmail(Email.of(userDto.getEmail())).orElseThrow(NotFoundUserException::new);

        if (!user.checkPassword(userDto.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return modelMapper.map(user, UserDto.Response.class);
    }

    public UserDto.Response update(long userId, UserDto.Updation userDto) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.update(userDto.getName());
        return modelMapper.map(user, UserDto.Response.class);
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }
}