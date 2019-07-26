package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

}
