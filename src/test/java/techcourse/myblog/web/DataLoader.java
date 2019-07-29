package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private UserDto dto = new UserDto();

    @Autowired
    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
        dto.setUsername("user");
        dto.setEmail("user@mail.net");
        dto.setPassword("aSdF12#$");
        userRepository.save(new User(dto));
    }
}
