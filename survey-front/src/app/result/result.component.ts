import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import {SurveyResultService} from "../questions/survey-result.service";
import {SurveyResult}        from "../model/survey-result";
import {WorkflowDataService} from "../workflow/workflow-data.service";

@Component({
  selector: 'result-app',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {

  constructor(private router: Router,
              private route: ActivatedRoute,
              private workflowDataService: WorkflowDataService,
              private surveyResultService: SurveyResultService) {}

  ngOnInit() {
    let answerId = this.workflowDataService.getAnswerId();
    this.route.parent.params.subscribe(params => {
      let surveyId = params['surveyId'];
      this.surveyResultService.getSurveyResult(surveyId, answerId).then(
        surveyResult => this.basicChart(surveyResult)
      );
    });
  }

  getTitle() {
    return this.workflowDataService.getStartData().title;
  }

  basicChart(surveyResult: SurveyResult) {
    const leftLeaning = surveyResult.leftLeaning;
    const rightLeaning = surveyResult.rightLeaning;
    const data = [{
      values: [leftLeaning, rightLeaning],
      labels: ['Left', 'Right'],
      type: 'pie'
    }];

    const layout = {
      height: 400,
      width: 500,
      paper_bgcolor: 'rgba(0,0,0,0)',
      plot_bgcolor: 'rgba(0,0,0,0)',
    };

    Plotly.newPlot('pie', data, layout, {staticPlot: true});
  }

  terminate() {
    this.workflowDataService.resetData();
    this.router.navigate(['']);
  }
}
