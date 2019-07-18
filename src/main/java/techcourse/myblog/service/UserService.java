package techcourse.myblog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        emailDuplicateValidate(user);
        userRepository.save(user);
        return user;
    }

    private void emailDuplicateValidate(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException();
        }
    }

    public Optional<User> authenticate(String email, String password) {
        return StreamSupport.stream(userRepository.findAll().spliterator(),false)
                .filter(a -> a.isSameMail(email) && a.isSamePassword(password))
                .findAny();
    }
}
