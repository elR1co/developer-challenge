package developer.challenge.survey.rest.repository;

import developer.challenge.survey.rest.model.UserSurveyAnswer;
import org.springframework.data.repository.CrudRepository;

public interface UserSurveyAnswerRepository extends CrudRepository<UserSurveyAnswer, Long> {
}
