package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.exception.UserArgumentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.service.exception.UserDeleteException;
import techcourse.myblog.service.exception.UserUpdateException;

import javax.transaction.Transactional;
import java.util.List;

import static techcourse.myblog.domain.exception.UserArgumentException.EMAIL_DUPLICATION_MESSAGE;
import static techcourse.myblog.domain.exception.UserArgumentException.PASSWORD_CONFIRM_FAIL_MESSAGE;

@Service
public class UserService {
	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(NotFoundUserException::new);
	}

	User findByUserPublicInfo(UserPublicInfoDto userPublicInfo) {
		return userRepository.findById(userPublicInfo.getId())
				.orElseThrow(NotFoundUserException::new);
	}

	public UserPublicInfoDto findUserPublicInfoById(Long id) {
		User user = findById(id);
		return new UserPublicInfoDto(user.getId(), user.getName(), user.getEmail());
	}

	public UserPublicInfoDto findUserPublicInfoByArticle(ArticleDto article) {
		User user = findById(article.getUserId());
		return new UserPublicInfoDto(user.getId(), user.getName(), user.getEmail());
	}

	public User save(UserDto userDto) {
		try {
			validate(userDto);
			return userRepository.save(userDto.toEntity());
		} catch (UserArgumentException e) {
			throw new SignUpException(e.getMessage());
		}
	}

	private void validate(UserDto userDto) {
		checkDuplicatedEmail(userDto.getEmail());
		checkPasswordConfirm(userDto);
	}

	private void checkDuplicatedEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new UserArgumentException(EMAIL_DUPLICATION_MESSAGE);
		}
	}

	private void checkPasswordConfirm(UserDto userDto) {
		if (!userDto.confirmPassword()) {
			throw new UserArgumentException(PASSWORD_CONFIRM_FAIL_MESSAGE);
		}
	}

	@Transactional
	public void update(UserPublicInfoDto userPublicInfoDto) {
		try {
			User user = findById(userPublicInfoDto.getId());
			user.updateName(userPublicInfoDto.getName());
		} catch (NotFoundUserException | UserArgumentException e) {
			throw new UserUpdateException(e.getMessage());
		}
	}

	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw new UserDeleteException(e.getMessage());
		}
	}
}
