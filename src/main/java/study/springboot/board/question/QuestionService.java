package study.springboot.board.question;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import study.springboot.board.DataNotFoundException;
import study.springboot.board.answer.Answer;
import study.springboot.board.answer.AnswerRepository;
import study.springboot.board.member.Member;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public Page<Question> getTotalQuestions(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> foundQuestion = questionRepository.findById(id);

        if (foundQuestion.isPresent()) {
            return foundQuestion.get();
        } else {
            throw new DataNotFoundException("해당 아이디의 질문이 없습니다.");
        }
    }

    public void createQuestion(String title, String content, Member author) {
        Question question = new Question(title, content, author);
        questionRepository.save(question);
    }

    public void modifyQuestion(Question question, String title, String content) {
        question.setTitle(title);
        question.setContent(content);
        questionRepository.save(question);
    }

    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @PostConstruct
    public void initData() {
        if (questionRepository.count() == 0) {
            createQuestion("스프링 부트가 무엇인가요?", "스프링 부트에 대해 알고 싶습니다.", null);
            createQuestion("스프링 MVC 모델 질문입니다.", "ID는 자동으로 생성되나요?", null);

            Optional<Question> foundQuestion = questionRepository.findById(2);

            if (foundQuestion.isPresent()) {
                Answer answer = new Answer("자동으로 생성됩니다.", foundQuestion.get());
                answerRepository.save(answer);
            }
        }
    }

}