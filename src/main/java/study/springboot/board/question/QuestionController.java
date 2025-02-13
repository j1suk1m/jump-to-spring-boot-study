package study.springboot.board.question;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.springboot.board.answer.AnswerForm;
import study.springboot.board.member.Member;
import study.springboot.board.member.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> paging = questionService.getTotalQuestions(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model model, @PathVariable Integer id, AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 메서드 실행
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 메서드 실행
    @PostMapping("/create")
    public String createQuestion(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Member member = memberService.findMember(principal.getName());
        questionService.createQuestion(questionForm.getTitle(), questionForm.getContent(), member);
        return "redirect:/question/list";
    }

}