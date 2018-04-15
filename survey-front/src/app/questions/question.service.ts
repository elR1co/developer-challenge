import { Injectable } from '@angular/core';

import {QUESTIONS} from "./mock-questions";
import {Question} from "../model/question";

@Injectable()
export class QuestionService {
  getQuestions(): Promise<Question[]> {
    return Promise.resolve(QUESTIONS);
  }
}
