import {Question} from "./question";

export class Survey {
  id: number;
  title: string;
  questions: Array<Question>;
}
