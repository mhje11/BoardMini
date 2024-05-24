package org.example.boardmini.repository;

import org.example.boardmini.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Iterable<Comment> findByBoardId(Long boardId);
}
