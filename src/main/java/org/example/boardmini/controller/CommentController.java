package org.example.boardmini.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.Comment;
import org.example.boardmini.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")

public class CommentController {
    private final CommentService commentService;
    @GetMapping("/comments/edit/{commentId}")
    public String editCommentForm(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);
        return "comment/commenteditform";
    }
    @PostMapping("/comments/edit/{commentId}")
    public String editComment(@PathVariable Long commentId, @ModelAttribute Comment comment, Model model) {
        Comment existingComment = commentService.findById(commentId);
        Long boardId = existingComment.getBoardId();
        if (existingComment.getPassword().equals(comment.getPassword())) {
            existingComment.setUsername(comment.getUsername());
            existingComment.setContent(comment.getContent());
            commentService.saveComment(existingComment);
            return "redirect:/list/view/" + boardId;
        }
        else {
            model.addAttribute("comment", existingComment);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "comment/commenteditform";
        }
    }

    @GetMapping("/comments/delete/{commentId}")
    public String deleteCommentForm(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);
        return "comment/commentdeleteform";
    }
    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @ModelAttribute Comment comment, Model model) {
        Comment existingComment = commentService.findById(commentId);
        Long boardId = existingComment.getBoardId();
        if (existingComment.getPassword().equals(comment.getPassword())) {
            commentService.deleteComment(commentId);
            return "redirect:/list/view/" + boardId;
        }
        else {
            model.addAttribute("comment", existingComment);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "comment/commentdeleteform";
        }
    }
}
