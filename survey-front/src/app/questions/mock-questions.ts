import {Question} from "../model/question";

export const QUESTIONS: Question[] = [
  { id: 1, title: 'How would you best describe an ideal government?',
    proposedAnswers: [
      { id: 1, text: 'Is progressive by nature' },
      { id: 2, text: 'Is conservative by nature' },
      { id: 3, text: 'Tends to look into the future' },
      { id: 4, text: 'Tends to look into the past' },
      { id: 5, text: 'Values meritocracy' },
      { id: 6, text: 'Values egalitarianism' },
      { id: 7, text: 'Promotes free trade' },
      { id: 8, text: 'Promotes fair trade' },
      { id: 9, text: 'Focuses on the individual' },
      { id: 10, text: 'Focuses on society' }
    ] },
  { id: 2, title: 'How would you best describe an ideal government?',
    proposedAnswers: [
      { id: 11, text: 'Self-reliance'},
      { id: 12, text: 'Self-nurturing'},
      { id: 13, text: 'Self-defence'},
      { id: 14, text: 'Openness'},
      { id: 15, text: 'Moral strength'},
      { id: 16, text: 'Empathy'},
      { id: 17, text: 'Self-discipline'},
      { id: 18, text: 'Self-examination'}
    ] },
  { id: 3, title: 'How would you best describe an ideal government?',
    proposedAnswers: [
      { id: 19, text: 'Aggression'},
      { id: 20, text: 'Diplomacy'},
      { id: 21, text: 'Upholding order'},
      { id: 22, text: 'Fairness'},
      { id: 23, text: 'Helping those who help themselves'},
      { id: 24, text: 'Helping those who cannot help themselves'},
      { id: 25, text: 'Champions of opportunity'},
      { id: 26, text: 'Champions of the downtrodden'}
    ] }
];
