package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("delete from User u where u.email = ?1")
    void deleteUserByEmailAddress(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.userName = ?1, u.password = ?2 where u.email = ?3")
    int updateUserByEmailAddress(String userName, String password, String email);

    User findByEmail(String email);

}
