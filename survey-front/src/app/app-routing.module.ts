import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {StartComponent} from "./start/start.component";
import {QuestionsComponent} from "./questions/questions.component";
import {ResultComponent} from "./result/result.component";
import {WorkflowGuard} from "./workflow/workflow-guard.service";
import {SurveyComponent} from "./survey/survey.component";

const routes: Routes = [
  { path: '', redirectTo: 'survey/1', pathMatch: 'full' },
  { path: 'survey/:surveyId',
    component: SurveyComponent,
    children: [
      {path: '', redirectTo: 'start', pathMatch: 'full' },
      { path: 'start', component: StartComponent },
      { path: 'questions', component: QuestionsComponent, canActivate: [WorkflowGuard] },
      { path: 'result', component: ResultComponent, canActivate: [WorkflowGuard] }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ WorkflowGuard ]
})
export class AppRoutingModule {}
