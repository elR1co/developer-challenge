import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule }    from '@angular/http';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';

import { AppComponent } from './app.component';
import {AppRoutingModule} from "./app-routing.module";
import {NavbarComponent} from "./navbar/navbar.component";
import {StartComponent} from "./start/start.component";
import {QuestionsComponent} from "./questions/questions.component";
import {ResultComponent} from "./result/result.component";
import {QuestionService} from "./questions/question.service";
import {SurveyService} from "./questions/survey.service";
import {AnswerService} from "./questions/answer.service";
import {SurveyResultService} from "./questions/survey-result.service";
import {WorkflowDataService} from "./workflow/workflow-data.service";
import {WorkflowStepService} from "./workflow/workflow-step.service";
import {SurveyComponent} from "./survey/survey.component";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    StartComponent,
    QuestionsComponent,
    ResultComponent,
    SurveyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    QuestionService,
    SurveyService,
    AnswerService,
    WorkflowDataService,
    WorkflowStepService,
    SurveyResultService,
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
