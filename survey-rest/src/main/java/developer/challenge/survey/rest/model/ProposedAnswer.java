package developer.challenge.survey.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "question")
@EqualsAndHashCode(of = "text")
public class ProposedAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NonNull
    private String text;

    @Column
    private double score;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="question_id")
    private Question question;

    public static class ProposedAnswerBuilder {

        private ProposedAnswer proposedAnswer;

        private ProposedAnswerBuilder(String text) {
            proposedAnswer = new ProposedAnswer(text);
        }

        public static ProposedAnswerBuilder withText(String text) {
            return new ProposedAnswerBuilder(text);
        }

        public ProposedAnswerBuilder withScore(double score) {
            proposedAnswer.setScore(score);
            return this;
        }

        public ProposedAnswer build() {
            return proposedAnswer;
        }
    }
}
