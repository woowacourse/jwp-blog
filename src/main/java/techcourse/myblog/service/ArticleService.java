package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.repository.ArticleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(int id) {
        return articleRepository.findById(id).orElseThrow(() -> {
            log.error("사용자를 찾을 수 없음 : id >>> {}", id);
            return new ArticleException("id를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void deleteById(int id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public Article write(ArticleDto articleDto) {
        Article article = modelMapper.map(articleDto, Article.class);
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(ArticleDto articleDto, int articleId) {
        Article dbArticle = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException("id를 찾을 수 없습니다."));

        dbArticle.updateArticle(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());

        return dbArticle;
    }
}
