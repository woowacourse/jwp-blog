package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;

public interface SnsInfoRepository extends CrudRepository<SnsInfo, Long> {
    void deleteByUserId(long id);
}
