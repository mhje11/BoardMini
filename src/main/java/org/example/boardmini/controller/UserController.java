package org.example.boardmini.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.User;
import org.example.boardmini.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("signUp")
    public String signUpForm(Model model) {
        model.addAttribute("user", new User());
        return "user/signUp";
    }
    @PostMapping("signUp")
    public String signUp(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findUserByUserId(user.getUserId());
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("error", "이미 존재하는 사용자 ID 입니다.");
            return "redirect:/signUp";
        }
        userService.saveUser(user);
        return "redirect:/";
    }
    @GetMapping("/")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }
    @PostMapping("/")
    public String login(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpSession session) {
        User loginUserInfo = userService.findByUserIdAndPassword(user.getUserId(), user.getPassword());
        if (loginUserInfo != null) {
            session.setAttribute("user", loginUserInfo);
            return "redirect:/list";
        } else {
            redirectAttributes.addFlashAttribute("error", "아이디나 비밀번호가 일치하지 않습니다.");
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/home")
    public String goToHome(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
