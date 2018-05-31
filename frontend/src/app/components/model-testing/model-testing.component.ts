import {Component, OnInit, Input} from '@angular/core';
import {TrainingData} from '../../_models/training';
import { ModelService } from '../../_services';

@Component({selector: 'app-model-testing', templateUrl: './model-testing.component.html', styleUrls: ['./model-testing.component.scss']})
export class ModelTestingComponent implements OnInit {
  @Input('trainingData')trainingData : [TrainingData];
  @Input('modelId')modelId : Number;


  selectedTrainingData: String = null;

  tdata: [{
    endTimeLong: number;
    endTime: String;
    url: String;
  }] = [{endTimeLong: 0, endTime: "", url: ""}];

  constructor(private modelService : ModelService) {}

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
    this.modelService.download(this.modelId, this.selectedTrainingData);
    // .subscribe(blob => {
    //   var link=document.createElement('a');
    //   link.href=window.URL.createObjectURL(blob);
    //   link.download="any name + extension";
    //   link.click();
    // });
  }

  test() {
    this.modelService.test(this.modelId, this.selectedTrainingData, 'Text')
    .subscribe(data => {
      console.log(data);
    })
  }

}
