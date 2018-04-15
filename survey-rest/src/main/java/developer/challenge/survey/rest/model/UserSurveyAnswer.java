package developer.challenge.survey.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
public class UserSurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            name="user_answer",
            joinColumns = @JoinColumn(name="user_survey_answer_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="proposed_answer_id", referencedColumnName="id" )
    )
    private Set<ProposedAnswer> userAnswers = new LinkedHashSet<>();

    public UserSurveyAnswer addAnswer(ProposedAnswer answer) {
        this.userAnswers.add(answer);
        return this;
    }

    // metadatas
    @Column(nullable = true) String ipAddress;
    @Column(nullable = true) String browser;
    @Column(nullable = true) String browserType;
    @Column(nullable = true) String deviceBrandName;
    @Column(nullable = true) String deviceCodeName;
    @Column(nullable = true) String deviceName;
    @Column(nullable = true) String deviceType;
    @Column(nullable = true) String platform;
    @Column(nullable = true) String platformMaker;
    @Column(nullable = true) String platformVersion;
}
