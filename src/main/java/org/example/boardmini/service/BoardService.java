package org.example.boardmini.service;

import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.Board;
import org.example.boardmini.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Iterable<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board saveBoard(Board board) {
        board.setCreated_at(LocalDateTime.now());
        board.setUpdated_at(LocalDateTime.now());
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> findAllBoards(Pageable pageable) {
        PageRequest sortedByDescId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findAll(sortedByDescId);

    }

    @Transactional
    public void likeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            Long currentLikes = board.getLikes();
            board.setLikes(currentLikes + 1);
            boardRepository.save(board);
        }
    }

    @Transactional
    public void unlikeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            Long currentLikes = board.getLikes();
            board.setLikes(currentLikes - 1);
            boardRepository.save(board);
        }
    }

}
