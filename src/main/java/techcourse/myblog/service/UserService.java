package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.AlreadyExistEmailException;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.NotMatchPasswordException;
import techcourse.myblog.service.request.UserChangeableInfoDto;
import techcourse.myblog.service.request.UserSignUpInfoDto;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void signUp(UserSignUpInfoDto userSignUpInfoDto) {
		Information information = userSignUpInfoDto.valueOfInfo();
		if (userRepository.findByInformationEmail(userSignUpInfoDto.getEmail()).isPresent()) {
			throw new AlreadyExistEmailException();
		}
		User user = new User(information);
		userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findUser(User user) {
		return userRepository.findByInformationEmail(user.getEmail())
				.orElseThrow(NotFoundUserException::new);
	}

	public void leaveUser(User loginUser, String password) {
		User user = findUser(loginUser);
		if (!user.matchPassword(password)) {
			throw new NotMatchPasswordException();
		}
		userRepository.delete(user);
	}

	public User editUser(User loginUser, UserChangeableInfoDto userChangeableInfoDto) {
		Information information = userChangeableInfoDto.valueOfInfo(loginUser);
		User user = findUser(loginUser);
		user.editUser(information);
		userRepository.save(user);
		return user;
	}
}
