package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.LoginRepository;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public boolean notExistUserEmail(String email) {
        return loginRepository.findUserByEmail(email) == null;
    }

    public boolean matchEmailAndPassword(String email, String password) {
        return loginRepository.findUserByEmail(email).matchPassword(password);
    }

    public User findUserByEmail(String email) {
        return loginRepository.findUserByEmail(email);
    }
}
