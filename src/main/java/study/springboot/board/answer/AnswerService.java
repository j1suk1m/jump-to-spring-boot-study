package study.springboot.board.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

}