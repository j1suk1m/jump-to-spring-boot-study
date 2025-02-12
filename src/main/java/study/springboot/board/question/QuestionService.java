package study.springboot.board.question;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.springboot.board.DataNotFoundException;
import study.springboot.board.answer.Answer;
import study.springboot.board.answer.AnswerRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public List<Question> getTotalQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> foundQuestion = questionRepository.findById(id);

        if (foundQuestion.isPresent()) {
            return foundQuestion.get();
        } else {
            throw new DataNotFoundException("해당 아이디의 질문이 없습니다.");
        }
    }

    @PostConstruct
    public void initData() {
        if (questionRepository.count() == 0) {
            Question question1 = new Question("스프링 부트가 무엇인가요?", "스프링 부트에 대해 알고 싶습니다.");
            Question question2 = new Question("스프링 MVC 모델 질문입니다.", "ID는 자동으로 생성되나요?");

            questionRepository.save(question1);
            questionRepository.save(question2);

            Optional<Question> foundQuestion = questionRepository.findById(2);

            if (foundQuestion.isPresent()) {
                Answer answer = new Answer("자동으로 생성됩니다.", foundQuestion.get());

                answerRepository.save(answer);
            }
        }
    }

}