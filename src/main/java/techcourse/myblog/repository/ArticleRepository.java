package techcourse.myblog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import techcourse.myblog.domain.Article;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
}
