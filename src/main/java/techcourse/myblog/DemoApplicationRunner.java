package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.support.encrytor.EncryptHelper;

@Component
@Profile("!test")
public class DemoApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptHelper encryptHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User("BROWN", "user@naver.com", encryptHelper.encrypt("Password123!"));
        User user2 = new User("CU", "user2@naver.com", encryptHelper.encrypt("Password123!"));
        userRepository.save(user);
        userRepository.save(user2);
    }
}
