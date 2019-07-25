package techcourse.myblog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
}
