package techcourse.myblog.domain;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleAssembler {
    public ArticleDTO writeDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setCoverUrl(article.getCoverUrl());
        articleDTO.setContents(article.getContents());
        return articleDTO;
    }

    public List<ArticleDTO> writeDTOs(List<Article> articles) {
        return articles.stream()
                .map(this::writeDTO)
                .collect(Collectors.toList());
    }

    public Article writeArticle(ArticleDTO articleDTO) {
        return new Article(articleDTO.getTitle(), articleDTO.getCoverUrl(), articleDTO.getContents());
    }
}
