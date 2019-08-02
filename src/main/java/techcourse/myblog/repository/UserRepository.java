package techcourse.myblog.repository;

import java.util.List;
import java.util.Optional;

import techcourse.myblog.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByInformation_Email(String email);

	List<User> findAll();
}
