package study.springboot.board.answer;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.springboot.board.DataNotFoundException;
import study.springboot.board.member.Member;
import study.springboot.board.question.Question;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(String content, Question question, Member author) {
        Answer answer = new Answer(content, question, author);
        answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);

        if (optionalAnswer.isPresent()) {
            return optionalAnswer.get();
        }

        throw new DataNotFoundException("Answer Not Found");
    }

    public void modifyAnswer(Answer answer, String content) {
        answer.setContent(content);
        answerRepository.save(answer);
    }

}