package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> ifLoggedIn(HttpSession session) {
        return userRepository.findByEmail((String) session.getAttribute("email"));
    }

    public boolean tryRender(Model model, HttpSession session) {
        return ifLoggedIn(session).map(user -> {
                                        model.addAttribute("user", user);
                                        return true;
                                    }).orElse(false);
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

    public void logout(HttpSession session) {
        session.invalidate();
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

    public UserQueryResult tryUpdate(String name, String email, HttpSession session) {
        return ifLoggedIn(session).map(user -> {
            if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
                return UserQueryResult.EMAIL_ALREADY_TAKEN;
            }
            try {
                user.update(name, email);
                session.setAttribute("name", name);
                session.setAttribute("email", email);
                return UserQueryResult.SUCCESS;
            } catch (IllegalArgumentException e) {
                return UserQueryResult.INVALID_INPUT;
            }
        }).orElse(UserQueryResult.IS_NOT_LOGGED_IN);
    }

    public void tryDelete(HttpSession session) {
        ifLoggedIn(session).ifPresent(user -> {
                                userRepository.delete(user);
                                session.invalidate();
                            });
    }
}