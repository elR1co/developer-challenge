import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {SurveyResult} from "../model/survey-result";


@Injectable()
export class SurveyResultService {

  private surveyUrl = 'api/surveys';

  constructor(private http: Http) { }

  getSurveyResult(surveyId: number, answerId: number): Promise<SurveyResult> {
    const url = `${this.surveyUrl}/${surveyId}/results/${answerId}`;
    return this.http.get(url)
      .toPromise()
      .then(
        response => response.json() as SurveyResult)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
