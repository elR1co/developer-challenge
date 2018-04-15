package developer.challenge.survey.rest.service;

import developer.challenge.survey.rest.model.ProposedAnswer;
import developer.challenge.survey.rest.model.Question;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SurveyCalculationEngineTest {

    SurveyCalculationEngine engine = new SurveyCalculationEngine();

    @Test public void should_return_an_array_of_doubles() {
        // Given
        ProposedAnswer answer1 = new ProposedAnswer() {{
            setId(1L);
            setScore(0);
            setQuestion(new Question() {{
                setWeight(2);
            }});
        }};
        ProposedAnswer answer2 = new ProposedAnswer() {{
            setId(2L);
            setScore(0.3);
            setQuestion(new Question() {{
                setWeight(1);
            }});
        }};
        ProposedAnswer answer3 = new ProposedAnswer() {{
            setId(3L);
            setScore(0.6);
            setQuestion(new Question() {{
                setWeight(1);
            }});
        }};

        // When
        int[] results = engine.calculate(Arrays.asList(answer1, answer2, answer3));

        // Then
        // (0*2 + 0.3*1 + 0.6*1) / (2+1+1) = 0.225 => 23% right and 77% left
        assertThat(results).containsExactly(77, 23);
    }
}
