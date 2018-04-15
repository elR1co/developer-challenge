import {ProposedAnswer} from "./proposed-answer";

export class Question {
  id: number;
  title: string;
  proposedAnswers: Array<ProposedAnswer>;
}
