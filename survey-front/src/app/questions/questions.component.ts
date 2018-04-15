import { Component, OnInit }   from '@angular/core';
import {QuestionService} from "./question.service";
import { Router, ActivatedRoute } from '@angular/router';
import {QUESTIONS} from "./mock-questions";
import {AnswerService} from "./answer.service";
import {Question} from "../model/question";
import {WorkflowDataService} from "../workflow/workflow-data.service";

@Component ({
  selector: 'msw-questions',
  templateUrl: './questions.component.html',
  styleUrls: ['./questions.component.css']
})

export class QuestionsComponent implements OnInit {

  questions: Question[];
  step: number = 0;
  validationError: boolean = false;
  answers: Object = {};

  constructor(private router: Router,
              private route: ActivatedRoute,
              private answerService: AnswerService,
              private workflowDataService: WorkflowDataService) {
  }

  ngOnInit(): void {
    this.questions = this.workflowDataService.getStartData().questions;
  }

  isCurrentStep(index: number): boolean {
    return this.step == index;
  }

  isFirstStep(): boolean {
    return this.step == 0;
  }

  isLastStep(): boolean {
    return this.questions && (this.step + 1 == this.questions.length);
  }

  back(): void {
    this.step -= 1;
    this.validationError = false;
  }

  next(): void {
    if (this.answers[this.questions[this.step].id]) {
      this.validationError = false;
      this.step += 1;
    } else {
      this.validationError = true;
    }
  }

  submit(): void {
    if (this.answers[this.questions[this.step].id]) {
      this.validationError = false;
      this.route.parent.params.subscribe(params => {
        let surveyId = params['surveyId'];
        this.answerService.postAnswers(surveyId, Object.values(this.answers)).then(
          answer => {
            this.workflowDataService.setAnswerId(answer.id);
            this.router.navigate(['../result'], { relativeTo: this.route });
          }
        );
      });
    } else {
      this.validationError = true;
    }
  }
}
