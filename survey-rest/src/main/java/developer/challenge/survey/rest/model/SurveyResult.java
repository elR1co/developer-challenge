package developer.challenge.survey.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyResult {

    private int leftLeaning;
    private int rightLeaning;
}
