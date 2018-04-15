package developer.challenge.survey.rest.repository;

import developer.challenge.survey.rest.model.Survey;
import org.springframework.data.repository.CrudRepository;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
}
