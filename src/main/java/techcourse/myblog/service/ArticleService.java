package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public Long save(ArticleDto articleDto) {
        User author = userRepository.findByEmail(articleDto.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Article newArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), author);
        articleRepository.save(newArticle);
        return newArticle.getId();
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Transactional
    public Article update(ArticleDto articleDto) {
        User author = userRepository.findByEmail(articleDto.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Article oldArticle = articleRepository.findById(articleDto.getId()).orElseThrow(() -> new ArticleNotFoundException("글을 찾을 수 없습니다."));
        Article updatedArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), author);
        return oldArticle.update(updatedArticle);
    }
}
