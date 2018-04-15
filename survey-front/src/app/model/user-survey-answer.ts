import {Question} from "./question";
import {ProposedAnswer} from "./proposed-answer";

export class UserSurveyAnswer {
  id: number;
  userAnswers: Array<ProposedAnswer>;
}
