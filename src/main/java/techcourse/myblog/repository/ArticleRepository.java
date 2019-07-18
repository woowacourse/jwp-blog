package techcourse.myblog.repository;

import techcourse.myblog.domain.Article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
