package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.dto.request.UserEditProfileDto;
import techcourse.myblog.exception.AlreadyExistEmailException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void signUp(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			throw new AlreadyExistEmailException();
		}
		User user = new User();
		user.saveUser(userDto);
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

	public User editUser(String email, UserEditProfileDto userEditProfileDto) {
		User user = findUser(email);
		user.editUser(userEditProfileDto);
		userRepository.save(user);
		return user;
	}
}
