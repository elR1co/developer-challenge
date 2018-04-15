import { Component, OnInit }   from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import {WorkflowDataService} from "../workflow/workflow-data.service";
import {SurveyService} from "../questions/survey.service";
import {Survey} from "../model/survey";

@Component({
  selector: 'start-app',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {

  survey: Survey;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private surveyService: SurveyService,
              private workflowDataService: WorkflowDataService) {}

  ngOnInit(): void {
    this.route.parent.params.subscribe(params => {
      let surveyId = params['surveyId'];
      this.surveyService.getSurvey(surveyId).then(survey => this.survey = survey);
    });
  }

  start() {
    this.workflowDataService.setStartData(this.survey);
    this.router.navigate(['../questions'], { relativeTo: this.route });
  }
}
