package developer.challenge.survey.rest.repository;

import developer.challenge.survey.rest.model.ProposedAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ProposedAnswerRepository extends CrudRepository<ProposedAnswer, Long> {

    Iterable<ProposedAnswer> findByIdIn(Collection<Long> ids);
}
