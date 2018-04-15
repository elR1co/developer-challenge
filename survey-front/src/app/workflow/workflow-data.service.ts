import { Injectable } from '@angular/core';
import {WorkflowStepService} from "./workflow-step.service";
import {STEPS} from "./workflow.model";
import {Survey} from "../model/survey";

@Injectable()
export class WorkflowDataService {

  private startData:Survey = null;
  private answerId:number = null;

  constructor(private workflowStepService: WorkflowStepService) {}

  setStartData(startData: Survey) {
    this.startData = startData;
    this.workflowStepService.validateStep(STEPS.start);
  }

  getStartData():Survey {
    return this.startData;
  }

  getAnswerId():number {
    return this.answerId;
  }

  setAnswerId(answerId: number) {
    this.answerId = answerId;
    this.workflowStepService.validateStep(STEPS.questions);
  }

  resetData(): void {
    this.answerId = null;
    this.startData = null;
    this.workflowStepService.resetSteps();
  }
}
