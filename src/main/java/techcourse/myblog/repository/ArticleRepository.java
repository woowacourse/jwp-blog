package techcourse.myblog.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.exception.NotFoundArticleException;

@Repository
public class ArticleRepository {
	private static int articleCount = 0;

	private List<Article> articles = new ArrayList<>();

	public List<Article> findAll() {
		return new ArrayList<>(articles);
	}

	public Article save(Article article) {
		article.setId(++articleCount);
		articles.add(article);
		return findById(articleCount);
	}

	public Article findById(long id) {
		return articles.stream()
				.filter(article -> article.matchId(id))
				.findFirst()
				.orElseThrow(NotFoundArticleException::new)
				;
	}

	public void update(Article modifiedArticle) {
		Article originArticle = findById(modifiedArticle.getId());
		originArticle.update(modifiedArticle);
	}

	public void deleteById(long articleId) {
		Article article = findById(articleId);
		articles.remove(article);
	}
}
