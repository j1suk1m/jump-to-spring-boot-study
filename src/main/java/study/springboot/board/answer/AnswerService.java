package study.springboot.board.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.springboot.board.question.Question;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(String content, Question question) {
        Answer answer = new Answer(content, question);
        answerRepository.save(answer);
    }

}