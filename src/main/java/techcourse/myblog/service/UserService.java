package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.exception.AlreadyExistEmailException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.user.Information;
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
		Information information = userSignUpInfo.valueOfInfo();
		if (userRepository.findByInformation_Email(userSignUpInfo.getEmail()).isPresent()) {
			throw new AlreadyExistEmailException();
		}
		User user = new User(information);
		userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findUser(String email) {
		return userRepository.findByInformation_Email(email)
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
		Information information = userChangeableInfo.valueOfInfo();
		User user = findUser(email);
		user.editUser(information);
		userRepository.save(user);
		return user;
	}
}
