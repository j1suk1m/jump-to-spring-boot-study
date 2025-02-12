package study.springboot.board.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.springboot.board.question.Question;
import study.springboot.board.question.QuestionService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(
            @PathVariable Integer id,
            @RequestParam String content,
            RedirectAttributes redirectAttributes) {
        Question question = questionService.getQuestion(id);
        answerService.create(content, question);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/detail/{id}";
    }

}