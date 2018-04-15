package developer.challenge.survey.rest.controller;

import developer.challenge.survey.rest.model.ProposedAnswer;
import developer.challenge.survey.rest.model.Survey;
import developer.challenge.survey.rest.model.SurveyResult;
import developer.challenge.survey.rest.model.UserSurveyAnswer;
import developer.challenge.survey.rest.repository.ProposedAnswerRepository;
import developer.challenge.survey.rest.repository.SurveyRepository;
import developer.challenge.survey.rest.repository.UserSurveyAnswerRepository;
import developer.challenge.survey.rest.service.SurveyCalculationEngine;
import developer.challenge.survey.rest.service.UserAgentParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    public static final String USER_AGENT = "User-Agent";

    @Autowired SurveyRepository surveyRepository;
    @Autowired UserSurveyAnswerRepository userSurveyAnswerRepository;
    @Autowired ProposedAnswerRepository proposedAnswerRepository;
    @Autowired SurveyCalculationEngine surveyCalculationEngine;
    @Autowired UserAgentParserService userAgentParserService;

    @RequestMapping(value = "", method = GET)
    public Iterable<Survey> getSurveys() {
        return surveyRepository.findAll();
    }

    @RequestMapping(value = "/{surveyId}", method = GET)
    public Survey getSurvey(@PathVariable Long surveyId) {
        return surveyRepository.findOne(surveyId);
    }

    @RequestMapping(value = "/{surveyId}/answers", method = POST)
    public ResponseEntity<UserSurveyAnswer> addSurveyResponses(@RequestBody List<Long> answerIds, @PathVariable Long surveyId, HttpServletRequest request) {
        // TODO: check ids belong to survey Id
        Iterable<ProposedAnswer> proposedAnswers = proposedAnswerRepository.findByIdIn(answerIds);
        UserSurveyAnswer userSurveyAnswer = userAgentParserService.parseUserAgent(Optional.ofNullable(request.getHeader(USER_AGENT)));
        userSurveyAnswer.setIpAddress(request.getRemoteAddr());
        proposedAnswers.forEach(userSurveyAnswer::addAnswer);
        this.userSurveyAnswerRepository.save(userSurveyAnswer);
        return ResponseEntity.ok(userSurveyAnswer);
    }

    @RequestMapping(value = "/{surveyId}/results/{answerId}", method = GET)
    public SurveyResult getSurveyResult(@PathVariable Long surveyId, @PathVariable Long answerId) {
        // TODO: check answerId is for survey with surveyId
        UserSurveyAnswer surveyAnswer = userSurveyAnswerRepository.findOne(answerId);
        int[] result = surveyCalculationEngine.calculate(surveyAnswer.getUserAnswers());
        return new SurveyResult(result[0], result[1]);
    }
}
