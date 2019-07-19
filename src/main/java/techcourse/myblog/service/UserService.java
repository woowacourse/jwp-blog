package techcourse.myblog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import techcourse.myblog.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
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
        System.out.println(userRepository.findByEmailAndPassword(email,password));
        return userRepository.findByEmailAndPassword(email,password);
    }
}
