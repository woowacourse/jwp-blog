package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    @Autowired
    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmailAndPassword(UserDto user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }
}
