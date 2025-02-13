package study.springboot.board.answer;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.springboot.board.member.Member;
import study.springboot.board.member.MemberService;
import study.springboot.board.question.Question;
import study.springboot.board.question.QuestionService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 메서드 실행
    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model,
            @PathVariable Integer id,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        Question question = questionService.getQuestion(id);
        Member member = memberService.findMember(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        answerService.create(answerForm.getContent(), question, member);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/detail/{id}";
    }

}