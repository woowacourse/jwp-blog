package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.UserRepository;

@Component
@Profile("!test")
public class DemoApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User("ike", "ike@gmail.com", "ASas!@12");
        User user2 = new User("ikee", "ike2@gmail.com", "ASas!@12");

        userRepository.save(user);
        userRepository.save(user2);
    }
}
