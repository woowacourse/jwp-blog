package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.exception.AlreadyExistEmailException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserChangeableInfo;
import techcourse.myblog.user.UserSignUpInfo;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void signUp(UserSignUpInfo userSignUpInfo) {
		if (userRepository.findByEmail(userSignUpInfo.getEmail()).isPresent()) {
			throw new AlreadyExistEmailException();
		}
		User user = userSignUpInfo.valueOfUser();
		user.saveUser(userSignUpInfo);
		userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findUser(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(NotFoundUserException::new);
	}

	public void leaveUser(String email, String password) {
		User user = findUser(email);
		if (!user.matchPassword(password)) {
			throw new NotMatchPasswordException();
		}
		userRepository.delete(user);
	}

	public User editUser(String email, UserChangeableInfo userChangeableInfo) {
		User user = findUser(email);
		user.editUser(userChangeableInfo);
		userRepository.save(user);
		return user;
	}
}
