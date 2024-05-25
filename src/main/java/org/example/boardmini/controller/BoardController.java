package org.example.boardmini.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.time.LocalDateTime;

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
        board.setViews(1L);
        board.setCreated_at(LocalDateTime.now());
        board.setUpdated_at(LocalDateTime.now());
        boardService.saveBoard(board);
        return "redirect:/list";
    }
    @GetMapping("/view/{id}")
    public String viewDetail(@PathVariable Long id,Model model) {
        Board board = boardService.findBoardById(id);
        boardService.plusViews(id);
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
            board.setViews(existingBoard.getViews());
            board.setCreated_at(existingBoard.getCreated_at());
            board.setUpdated_at(LocalDateTime.now());
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
            Iterable<Comment> comments = commentService.findByBoardId(id);
            if (comments != null) {
                for (Comment comment : comments) {
                    commentService.deleteComment(comment.getId());
                }
            }
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
    public String likeBoard(@PathVariable Long boardId, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        String cookieName = "liked_" + boardId + "_" + ipAddress.replace(":", "-");
        //cookie 이름엔 : 들어갈수없음
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName) && "true".equals(cookie.getValue())) {
                    boardService.unlikeBoard(boardId);
                    cookie.setValue("false");
                    response.addCookie(cookie);
                    boardService.minusViews(boardId);
                    return "redirect:/list/view/"+boardId;
                }
            }
        }
        boardService.likeBoard(boardId);
        Cookie likeCookie = new Cookie(cookieName, "true");
        likeCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(likeCookie);
        boardService.minusViews(boardId);
        return "redirect:/list/view/"+boardId;
    }
}
