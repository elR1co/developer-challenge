package developer.challenge.survey.rest.service;

import developer.challenge.survey.rest.model.ProposedAnswer;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class SurveyCalculationEngine {

    Mean mean = new Mean();

    public int[] calculate(Collection<ProposedAnswer> answers) {
        double[] values = new double[answers.size()];
        double[] weights = new double[answers.size()];

        int cpt = 0;
        for (ProposedAnswer answer : answers) {
            values[cpt] = answer.getScore();
            weights[cpt] = answer.getQuestion().getWeight();
            cpt++;
        }

        BigDecimal rightResult = BigDecimal.valueOf(mean.evaluate(values, weights)).setScale(3, ROUND_HALF_UP);
        int rightPercentage = rightResult.multiply(BigDecimal.valueOf(100)).setScale(0, ROUND_HALF_UP).intValue();
        int leftPercentage = 100 - rightPercentage;
        return new int[] {leftPercentage, rightPercentage};
    }

}
