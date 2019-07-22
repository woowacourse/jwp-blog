package techcourse.myblog.domain.user;

import org.springframework.data.repository.CrudRepository;

public interface SnsInfoRepository extends CrudRepository<SnsInfo, Long> {
    void deleteByUserId(long id);
}
