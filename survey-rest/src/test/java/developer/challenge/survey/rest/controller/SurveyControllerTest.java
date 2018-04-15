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
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static developer.challenge.survey.rest.controller.SurveyController.USER_AGENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SurveyControllerTest {

    @InjectMocks SurveyController surveyController;

    @Mock SurveyRepository surveyRepositoryMock;
    @Mock UserSurveyAnswerRepository userSurveyAnswerRepositoryMock;
    @Mock ProposedAnswerRepository proposedAnswerRepositoryMock;
    @Mock SurveyCalculationEngine surveyCalculationEngineMock;
    @Mock UserAgentParserService userAgentParserServiceMock;
    @Mock HttpServletRequest httpServletRequestMock;

    @Test public void should_get_surveys() {
        // Given
        when(surveyRepositoryMock.findAll()).thenReturn(Collections.singletonList(new Survey("text")));

        // When
        Iterable<Survey> result = surveyController.getSurveys();

        // Then
        assertThat(result).hasSize(1);
    }

    @Test public void should_get_survey() {
        // Given
        Long surveyId = 1L;

        Survey expectedSurvey = new Survey("text");
        when(surveyRepositoryMock.findOne(surveyId)).thenReturn(expectedSurvey);

        // When
        Survey result = surveyController.getSurvey(surveyId);

        // Then
        assertThat(result).isEqualTo(expectedSurvey);
    }

    @Test public void should_add_survey_responses() {
        // Given
        Long surveyId = 1L;
        List<Long> answerIds = Collections.singletonList(1L);

        String userAgent = "Safari...";
        UserSurveyAnswer expectedAnswer = new UserSurveyAnswer();
        when(proposedAnswerRepositoryMock.findByIdIn(answerIds)).thenReturn(Collections.singletonList(new ProposedAnswer("text")));
        when(httpServletRequestMock.getHeader(USER_AGENT)).thenReturn(userAgent);
        when(userAgentParserServiceMock.parseUserAgent(Optional.of(userAgent))).thenReturn(expectedAnswer);
        when(userSurveyAnswerRepositoryMock.save(expectedAnswer)).thenReturn(expectedAnswer);

        // When
        ResponseEntity<UserSurveyAnswer> result = surveyController.addSurveyResponses(answerIds, surveyId, httpServletRequestMock);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(expectedAnswer);
    }

    @Test public void should_get_survey_result() {
        // Given
        Long surveyId = 1L;
        Long answerId = 1L;

        int[] expectedLeanings = new int[2];
        UserSurveyAnswer expectedAnswer = new UserSurveyAnswer() {{
            addAnswer(new ProposedAnswer("text1"));
            addAnswer(new ProposedAnswer("text2"));
        }};

        when(userSurveyAnswerRepositoryMock.findOne(answerId)).thenReturn(expectedAnswer);
        when(surveyCalculationEngineMock.calculate(expectedAnswer.getUserAnswers())).thenReturn(expectedLeanings);

        // When
        SurveyResult result = surveyController.getSurveyResult(surveyId, answerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLeftLeaning()).isEqualTo(expectedLeanings[0]);
        assertThat(result.getRightLeaning()).isEqualTo(expectedLeanings[1]);
    }
}