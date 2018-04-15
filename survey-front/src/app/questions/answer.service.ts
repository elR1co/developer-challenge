import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {UserSurveyAnswer} from "../model/user-survey-answer";

@Injectable()
export class AnswerService {

  private surveyUrl = 'api/surveys';

  constructor(private http: Http) { }

  postAnswers(surveyId: number, answers: Array<number>): Promise<UserSurveyAnswer> {
    const url = `${this.surveyUrl}/${surveyId}/answers`;
    return this.http.post(url, answers)
      .toPromise()
      .then(
        response => response.json() as UserSurveyAnswer)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
