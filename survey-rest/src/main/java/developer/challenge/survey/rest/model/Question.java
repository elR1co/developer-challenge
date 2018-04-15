package developer.challenge.survey.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(exclude = "survey")
@EqualsAndHashCode(of = "title")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NonNull
    private String title;

    @Column
    private int weight;

    @JsonIgnore
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question", fetch = EAGER)
    private Set<ProposedAnswer> proposedAnswers = new LinkedHashSet<>();

    public Question addProposedAnswer(ProposedAnswer proposedAnswer) {
        proposedAnswer.setQuestion(this);
        this.proposedAnswers.add(proposedAnswer);
        return this;
    }
}
