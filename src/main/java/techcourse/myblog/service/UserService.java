package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;
import techcourse.myblog.exception.UserCreationException;
import techcourse.myblog.exception.UserUpdateException;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(final UserRequestDto.SignUpRequestDto signUpRequestDto) {
		return userRepository.save(signUpRequestDto.toUser());
	}

	public User update(final UserRequestDto.UpdateRequestDto updateRequestDto) {
		User user = userRepository.findByEmail(updateRequestDto.getEmail())
				.orElseThrow(NoSuchElementException::new);

		try {
			User userUpdated = new User(user.getId(), updateRequestDto.getName(), updateRequestDto.getEmail(), user.getPassword());
			return userRepository.save(userUpdated);
		} catch (UserCreationException e) {
			throw new UserUpdateException(e.getMessage());
		}
	}

	public User findById(final long id) {
		return userRepository.findById(id)
				.orElseThrow(NoSuchElementException::new);
	}

	public boolean exitsByEmail(final UserRequestDto.SignUpRequestDto signUpRequestDto) {
		try {
			return userRepository.existsByEmail(signUpRequestDto.getEmail());
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void deleteByEmail(final String email) {
		User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
		userRepository.deleteById(user.getId());
	}

	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}
}
