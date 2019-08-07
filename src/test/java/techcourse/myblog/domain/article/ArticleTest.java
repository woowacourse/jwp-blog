package techcourse.myblog.domain.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    private Article article;
    private User author;

    @BeforeEach
    public void setUp() {
	author = new User("name", "test@test.test", "passWORD1!");
	article = new Article("title", "url", "contents", author);
    }

    @Test
    public void updateArticle() {
	String newTitle = "new Title";
	String newCoverUrl = "new CoverUrl";
	String newContents = "new Contents";
	ArticleDto updatedArticleDto = new ArticleDto(1L, author.getId(), newTitle, newCoverUrl, newContents);

	article.updateArticle(updatedArticleDto.toEntity(author));

	assertThat(article.getTitle()).isEqualTo(newTitle);
	assertThat(article.getCoverUrl()).isEqualTo(newCoverUrl);
	assertThat(article.getContents()).isEqualTo(newContents);
    }

    @Test
    @DisplayName("유저가 다르면 Article 수정 실패")
    public void updateArticleFail() {
	String newTitle = "new Title";
	String newCoverUrl = "new CoverUrl";
	String newContents = "new Contents";

	ArticleDto updatedArticleDto = new ArticleDto(1L, null, newTitle, newCoverUrl, newContents);
	User other = new User("name", "other@test.test", "passWORD1!");

	article.updateArticle(updatedArticleDto.toEntity(other));

	assertThat(article.getTitle()).isNotEqualTo(newTitle);
	assertThat(article.getCoverUrl()).isNotEqualTo(newCoverUrl);
	assertThat(article.getContents()).isNotEqualTo(newContents);
    }
}
