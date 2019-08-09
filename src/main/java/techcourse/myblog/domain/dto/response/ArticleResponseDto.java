package techcourse.myblog.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.article.Article;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {
	private Long articleId;
	private String title;
	private String coverUrl;
	private String contents;

	public static ArticleResponseDto toArticleResponseDto(Article article) {
		return new ArticleResponseDto(article.getArticleId(), article.getTitle(),
				article.getCoverUrl(), article.getContents());
	}
}
