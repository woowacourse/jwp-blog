package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

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

    public boolean tryLogin(String email, String password, HttpSession session) {
        return userRepository.findByEmail(email)
                            .filter(user -> user.authenticate(password))
                            .map(user -> {
                                session.setAttribute("name", user.getName());
                                session.setAttribute("email", user.getEmail());
                                return true;
                            }).orElse(false);
    }

    public Iterable<User> loadEveryUsers() {
        return userRepository.findAll();
    }


    public UserQueryResult tryRegister(String name, String email, String password) {
        return userRepository.findByEmail(email).map(ifSameEmailExists -> UserQueryResult.EMAIL_ALREADY_TAKEN)
                                                .orElseGet(() -> {
                                                    try {
                                                        userRepository.save(new User(name, email, password));
                                                        return UserQueryResult.SUCCESS;
                                                    } catch(IllegalArgumentException e) {
                                                        return UserQueryResult.INVALID_INPUT;
                                                    }
                                                });
    }

    @Transactional
    public UserQueryResult tryUpdate(String editedName, String editedEmail, String currentEmail) {
        if (!editedEmail.equals(currentEmail) && userRepository.findByEmail(editedEmail).isPresent()) {
            return UserQueryResult.EMAIL_ALREADY_TAKEN;
        }
        try {
            userRepository.findByEmail(currentEmail).get().update(editedName, editedEmail);
            return UserQueryResult.SUCCESS;
        } catch (IllegalArgumentException e) {
            return UserQueryResult.INVALID_INPUT;
        }
    }

    @Transactional
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }
}