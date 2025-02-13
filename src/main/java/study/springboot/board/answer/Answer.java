package study.springboot.board.answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import study.springboot.board.member.Member;
import study.springboot.board.question.Question;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Member author;

    public Answer(String content, Question question) {
        this(content, question, null);
    }

    public Answer(String content, Question question, Member author) {
        this.content = content;
        this.question = question;
        this.author = author;
    }

}