package developer.challenge.survey.rest;

import developer.challenge.survey.rest.model.ProposedAnswer.ProposedAnswerBuilder;
import developer.challenge.survey.rest.model.Question;
import developer.challenge.survey.rest.model.Survey;
import developer.challenge.survey.rest.repository.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(SurveyRepository repository) {
		return (args) -> {
			// create questions and associated proposed answers
			Question question1 = new Question("How would you best describe an ideal government?");
			question1.setWeight(2);
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Is progressive by nature").withScore(0).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Is conservative by nature").withScore(1).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Tends to look into the future").withScore(0.1).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Tends to look into the past").withScore(0.9).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Values meritocracy").withScore(0.2).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Values egalitarianism").withScore(0.8).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Promotes free trade").withScore(0.3).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Promotes fair trade").withScore(0.7).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Focuses on the individual").withScore(0.4).build());
			question1.addProposedAnswer(ProposedAnswerBuilder.withText("Focuses on society").withScore(0.6).build());

			Question question2 = new Question("Which traits would you pass on to your child?");
			question2.setWeight(1);
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Self-reliance").withScore(0.6).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Self-nurturing").withScore(0.2).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Self-defence").withScore(0.6).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Openness").withScore(0.2).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Moral strength").withScore(0.6).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Empathy").withScore(0.2).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Self-discipline").withScore(0.6).build());
			question2.addProposedAnswer(ProposedAnswerBuilder.withText("Self-examination").withScore(0.2).build());

			Question question3 = new Question("Would you rather vote for:");
			question3.setWeight(1);
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Aggression").withScore(1).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Diplomacy").withScore(0).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Upholding order").withScore(0.8).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Fairness").withScore(0.2).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Helping those who help themselves").withScore(0.6).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Helping those who cannot help themselves").withScore(0.4).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Champions of opportunity").withScore(0.6).build());
			question3.addProposedAnswer(ProposedAnswerBuilder.withText("Champions of the downtrodden").withScore(0.4).build());

			// create a survey
			Survey survey = new Survey("Political Leanings");
			survey.add(question1);
			survey.add(question2);
			survey.add(question3);

			// save it
			repository.save(survey);

			log.info("Saved Survey : " + survey);
		};
	}

}