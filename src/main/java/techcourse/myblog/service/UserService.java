package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.exception.NotMatchUserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto.Response login(UserDto.Login userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(NotFoundUserException::new);
        if (!user.getPassword().equals(userDto.getPassword())) {
            throw new NotMatchPasswordException();
        }

        return modelMapper.map(user, UserDto.Response.class);
    }

    public Long save(UserDto.Create userDto) {
        User newUser = userDto.toUser();
        userRepository.findByEmail(newUser.getEmail()).ifPresent((user) -> {
            throw new DuplicatedUserException();
        });
        return userRepository.save(newUser).getId();
    }

    public List<UserDto.Response> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
            .map(user -> modelMapper.map(user, UserDto.Response.class))
            .collect(Collectors.toList());
    }

    public UserDto.Response findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        return modelMapper.map(user, UserDto.Response.class);
    }

    public UserDto.Response findById(UserDto.Response userSession, Long userId) {
        checkUser(userSession, userId);
        return findById(userId);
    }

    public UserDto.Response update(UserDto.Response userSession, Long userId, UserDto.Update userDto) {
        checkUser(userSession, userId);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.update(userDto);
        return modelMapper.map(user, UserDto.Response.class);
    }

    public void deleteById(UserDto.Response userSession, Long userId) {
        checkUser(userSession, userId);
        userRepository.deleteById(userId);
    }

    //TODO : Update에서 ID를 가져야할거 같음
    private void checkUser(UserDto.Response userSession, Long userId) {
        if (!(userSession.getId() == userId)) {
            throw new NotMatchUserException();
        }
    }
}
