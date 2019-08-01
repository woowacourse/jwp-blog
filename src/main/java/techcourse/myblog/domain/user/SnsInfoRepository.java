package techcourse.myblog.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SnsInfoRepository extends CrudRepository<SnsInfo, Long> {
    void deleteByUserId(long id);

    List<SnsInfo> findByUserId(long id);

    Optional<SnsInfo> findBySnsCode(long id);
}
