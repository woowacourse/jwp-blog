package techcourse.myblog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import techcourse.myblog.domain.article.Article;

public interface ArticlePageableRepository extends PagingAndSortingRepository<Article, Long> {

}
