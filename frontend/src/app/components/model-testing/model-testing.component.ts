import {Component, OnInit, Input} from '@angular/core';
import {TrainingData} from '../../_models/training';

@Component({selector: 'app-model-testing', templateUrl: './model-testing.component.html', styleUrls: ['./model-testing.component.scss']})
export class ModelTestingComponent implements OnInit {
  @Input('trainingData')trainingData : [TrainingData];
  @Input('modelId')modelId : String;


  selectedTrainingData: String = null;

  tdata: [{
    endTimeLong: number;
    endTime: String;
    url: String;
  }] = [{endTimeLong: 0, endTime: "", url: ""}];

  constructor() {}

  ngOnInit() {
    this.tdata.pop();
    this.trainingData.forEach(element => {
      let d: Date = new Date(element.lastTrainingEnd);
      this.tdata.push({
        endTimeLong: element.lastTrainingEnd,
        endTime: d.toDateString() + ' ' + d.toLocaleTimeString(),
        url: element.modelUrl
      })
    });
    console.log(this.tdata);
    
  }

  download() {
    console.log(this.selectedTrainingData);
    
  }

}
