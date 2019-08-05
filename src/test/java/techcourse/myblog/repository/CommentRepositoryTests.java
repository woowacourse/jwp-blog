package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.Contents;
import techcourse.myblog.domain.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CommentRepositoryTests {
	private User user;
	private Comment actualComment;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		user = userRepository.findById(1L).get();

		Contents contents = new Contents("contents");
		actualComment = new Comment(user, contents);
		actualComment = commentRepository.save(actualComment);
	}

	@Test
	void findById() {
		Comment expectComment = commentRepository.findById(actualComment.getId()).get();
		assertTrue(actualComment.matchComment(expectComment));
	}
}