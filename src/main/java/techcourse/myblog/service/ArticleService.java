package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void update(ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(articleRequestDto.getId()).orElseThrow(ArticleNotFoundException::new);
        article.update(articleRequestDto);
    }
}