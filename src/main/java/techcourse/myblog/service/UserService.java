package techcourse.myblog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        userDto.setPassword(userDto.getPassword());
        User user = new User(userDto.getName(),userDto.getEmail(),userDto.getPassword());
        userRepository.save(user);
        return user;
    }

    private void emailDuplicateValidate(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException();
        }
    }

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email,password);
    }

    public boolean isMatchPassword(UserDto userDto){
        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());
        if(maybeUser.isPresent()){
            return isSamePassword(maybeUser.get());
        }
        throw new IllegalArgumentException();
    }

    private boolean isSamePassword(User userDto) {
        return userDto.isSamePassword(userDto);
    }
}
