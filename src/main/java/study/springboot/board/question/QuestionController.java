package study.springboot.board.question;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @PreAuthorize("isAuthenticated()") // 로그인한 경우에만 메서드 실행
    @GetMapping("/modify/{id}")
    public String modifyQuestion(
            QuestionForm questionForm,
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = questionService.getQuestion(id);

        // 현재 로그인한 사용자와 질문 작성자가 동일하지 않은 경우
        if (!question.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionForm.setTitle(question.getTitle());
        questionForm.setContent(question.getContent());

        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyQuestion(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Principal principal,
            @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Question question = questionService.getQuestion(id);

        // 현재 로그인한 사용자와 질문 작성자가 동일하지 않은 경우
        if (!question.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionService.modifyQuestion(question, questionForm.getTitle(), questionForm.getContent());
        redirectAttributes.addAttribute("id", id);

        return "redirect:/question/detail/{id}";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteQuestion(Principal principal, @PathVariable Integer id) {
        Question question = questionService.getQuestion(id);

        // 현재 로그인한 사용자와 질문 작성자가 동일하지 않은 경우
        if (!question.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        questionService.deleteQuestion(question);

        return "redirect:/";
    }

}