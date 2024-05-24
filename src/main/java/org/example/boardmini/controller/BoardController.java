package org.example.boardmini.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.Board;
import org.example.boardmini.domain.Comment;
import org.example.boardmini.service.BoardService;
import org.example.boardmini.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public String boards(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        return "board/list";
    }
    @GetMapping("/writeform")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/add";
    }
    @PostMapping("/writeform")
    public String write(@ModelAttribute Board board) {
        boardService.saveBoard(board);
        return "redirect:/list";
    }
    @GetMapping("/view/{id}")
    public String viewDetail(@PathVariable Long id,Model model) {
        Board board = boardService.findBoardById(id);
        Iterable<Comment> comments = commentService.findByBoardId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("board", board);
        model.addAttribute("newComment", new Comment());
        return "board/detail";
    }

    @GetMapping("/updateform/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute(board);
        return "board/editform";
    }
    @PostMapping("/updateform/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Board board, Model model) {
        Board existingBoard = boardService.findBoardById(id);
        if (board.getPassword().equals(existingBoard.getPassword())) {
            boardService.saveBoard(board);
            return "redirect:/list";
        } else {
            model.addAttribute("board", existingBoard);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "board/editform";
        }
    }
    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "board/deleteform";
    }
    @PostMapping("/deleteform/{id}")
    public String delete(@PathVariable Long id, @ModelAttribute Board board, Model model) {
        Board existingBoard = boardService.findBoardById(id);
        if (existingBoard.getPassword().equals(board.getPassword())) {
            boardService.deleteBoard(id);
            return "redirect:/list";
        } else {
            model.addAttribute("board", board);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "board/deleteform";
        }
    }
    @PostMapping("/comments/{boardId}")
    public String addComment(@PathVariable Long boardId, @ModelAttribute Comment comment) {
        comment.setBoardId(boardId);
        commentService.saveComment(comment);
        return "redirect:/list/view/" +boardId;
    }
    @PostMapping("/like/{boardId}")
    public String likeBoard(@PathVariable Long boardId) {
        boardService.likeBoard(boardId);
        return "redirect:/list/view/"+boardId;
    }


}
