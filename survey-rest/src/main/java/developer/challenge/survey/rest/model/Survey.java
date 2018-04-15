package developer.challenge.survey.rest.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String title;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "survey", fetch = EAGER)
    private Set<Question> questions = new LinkedHashSet<>();

    public Survey add(Question question) {
        question.setSurvey(this);
        this.questions.add(question);
        return this;
    }
}
