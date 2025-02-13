package study.springboot.board.answer;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyAnswer(
            AnswerForm answerForm,
            @PathVariable Integer id,
            Principal principal
    ) {
        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerForm.setContent(answer.getContent());

        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyAnswer(
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            @PathVariable Integer id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerService.modifyAnswer(answer, answerForm.getContent());
        redirectAttributes.addAttribute("id", answer.getQuestion().getId());

        return "redirect:/question/detail/{id}";
    }

}