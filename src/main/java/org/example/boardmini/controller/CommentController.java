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
        return "board/commenteditform";
    }
    @PostMapping("/comments/edit/{commentId}")
    public String editComment(@PathVariable Long commentId, @ModelAttribute Comment comment) {
        Comment existingComment = commentService.findById(commentId);
        existingComment.setContent(comment.getContent());
        commentService.saveComment(existingComment);
        return "redirect:/list/view/" + existingComment.getBoardId();
    }

    @GetMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        Long boardId = comment.getBoardId();
        commentService.deleteComment(commentId);
        return "redirect:/list/view/" +boardId;
    }
}
