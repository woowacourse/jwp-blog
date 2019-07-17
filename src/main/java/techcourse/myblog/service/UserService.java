package techcourse.myblog.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {
        StreamSupport.stream(userRepository.findAll().spliterator(), false)
        .filter(a -> a.isSameMail(user))
        .findAny().ifPresent(a -> {
            throw new IllegalArgumentException();
        });
    }
}
