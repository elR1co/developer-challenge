import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {Survey} from "../model/survey";


@Injectable()
export class SurveyService {

  private surveysUrl = 'api/surveys';

  constructor(private http: Http) { }

  getSurvey(surveyId: number): Promise<Survey> {
    const url = `${this.surveysUrl}/${surveyId}`;
    return this.http.get(url)
      .toPromise()
      .then(
        response => response.json() as Survey)
      .catch(this.handleError);
  }

  getSurveys(): Promise<Survey[]> {
    return this.http.get(this.surveysUrl)
      .toPromise()
      .then(
        response => response.json() as Survey[])
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
