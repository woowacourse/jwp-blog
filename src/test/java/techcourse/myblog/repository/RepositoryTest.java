package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	private User user;
	private Article article;
	private Comment comment;

	@BeforeEach
	void setUp() {
		user = userRepository.findById(1L).get();
		article = Article.builder()
				.title("title")
				.coverUrl("coverUrl")
				.contents("contents")
				.author(user)
				.build();

		article = articleRepository.save(article);

		comment = Comment.builder()
				.contents("댓글 입니다.")
				.author(user)
				.build();

		comment = commentRepository.save(comment);
		article.addComment(comment);
	}

	@Test
	void 아티클의_작성자_확인() {
		assertEquals(user, article.getAuthor());
	}

	@Test
	void 유저_이름_변경시_아티클에_적용_되는지_확인() {
		user.changeUserName("change");
		userRepository.save(user);

		User changeUser = articleRepository.findById(article.getArticleId()).get().getAuthor();
		assertEquals(changeUser.getUserName(), "change");
	}

	@Test
	void 댓글_수정시_아티클안에_있는_댓글_변경_확인() {
		article.addComment(comment);
		Comment comment2 = Comment.builder()
				.contents("수정된 댓글입니다.")
				.author(user)
				.build();
		comment.update(comment2, user);
		commentRepository.save(comment);
		List<Comment> comments = articleRepository.findById(article.getArticleId()).get().getComments();
		assertEquals(comments.get(0).getContents(), "수정된 댓글입니다.");
	}

	@Test
	void 글_삭제시_댓글삭제_확인() {
		articleRepository.deleteById(article.getArticleId());
		assertEquals(commentRepository.findById(comment.getId()), Optional.empty());
	}

}
