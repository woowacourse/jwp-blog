package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserReadService {
    private final UserRepository userRepository;
    
    public UserReadService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Transactional
    public Optional<User> findByEmailAndPassword(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
