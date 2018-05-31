import {Component, OnInit, Input} from '@angular/core';
import {TrainingData} from '../../_models/training';

@Component({selector: 'app-model-testing', templateUrl: './model-testing.component.html', styleUrls: ['./model-testing.component.scss']})
export class ModelTestingComponent implements OnInit {
  @Input('trainingData')trainingData : [TrainingData];


  selectedTrainingData: String;

  tdata: [{
    endTimeLong: number;
    endTime: String;
    url: String;
  }] = [{endTimeLong: 0, endTime: "", url: ""}];

  constructor() {}

  ngOnInit() {
    this.tdata.pop();
    this.trainingData.forEach(element => {
      this.tdata.push({
        endTimeLong: element.lastTrainingEnd,
        endTime: new Date(element.lastTrainingEnd).toDateString(),
        url: element.modelUrl
      })
    });
    console.log(this.tdata);
    
  }

  download() {
    console.log(this.selectedTrainingData);
    
  }

}
