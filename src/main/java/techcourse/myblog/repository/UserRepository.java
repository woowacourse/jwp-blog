package techcourse.myblog.repository;

import java.util.Optional;

import techcourse.myblog.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
