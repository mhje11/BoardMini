package org.example.boardmini.service;

import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.Comment;
import org.example.boardmini.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Iterable<Comment> findByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
