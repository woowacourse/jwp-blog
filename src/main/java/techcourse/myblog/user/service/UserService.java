package techcourse.myblog.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.dto.UserDto;
import techcourse.myblog.user.exception.DuplicatedUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.NotMatchPasswordException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void save(UserDto.Creation userDto) {
        User newUser = userDto.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        userRepository.save(newUser);
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
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(NotFoundUserException::new);

        if (!user.getPassword().equals(userDto.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return modelMapper.map(user, UserDto.Response.class);
    }

    public UserDto.Response update(long userId, UserDto.Updation userDto) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        User updatedUser = userDto.toUser(userId, user.getEmail(), user.getPassword());
        return modelMapper.map(userRepository.save(updatedUser), UserDto.Response.class);
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }
}
