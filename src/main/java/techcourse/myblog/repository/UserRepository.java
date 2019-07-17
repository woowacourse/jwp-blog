package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = ?1")
    User findUserByEmailAddress(String email);

    @Transactional
    @Modifying
    @Query("delete from User u where u.email = ?1")
    void deleteUserByEmailAddress(String email);
}
