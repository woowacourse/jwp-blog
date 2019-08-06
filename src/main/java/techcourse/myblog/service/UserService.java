package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.web.exception.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public void tryLogin(String email, String password, HttpSession session) {
        userRepository.findByEmail(email)
                     .filter(user -> user.authenticate(password))
                     .map(user -> {
                         session.setAttribute("name", user.getName());
                         session.setAttribute("email", user.getEmail());
                         return true;
                     }).orElseThrow(NoMatchingCredentialLoginException::new);
    }

    public Iterable<User> loadEveryUsers() {
        return userRepository.findAll();
    }

    public void tryRegister(String name, String email, String password) {
        userRepository.findByEmail(email).map(ifSameEmailExists -> {
                                                throw new EmailAlreadyTakenSignupException();
                                            }).orElseGet(() -> {
                                                try {
                                                    userRepository.save(new User(name, email, password));
                                                } catch (IllegalArgumentException e) {
                                                    throw new IllegalArgumentSignupException();
                                                }
                                                return true;
                                            });
    }

    @Transactional
    public void tryUpdate(String editedName, String editedEmail, String currentEmail) {
        if (!editedEmail.equals(currentEmail) && userRepository.findByEmail(editedEmail).isPresent()) {
            throw new EmailAlreadyTakenProfileEditException();
        }
        try {
            userRepository.findByEmail(currentEmail).get().update(editedName, editedEmail);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentProfileEditException();
        }
    }

    @Transactional
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }
}