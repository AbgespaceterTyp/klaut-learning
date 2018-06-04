import {Component, OnInit, Input} from '@angular/core';
import {TrainingData} from '../../_models/training';
import { ModelService } from '../../_services';

@Component({selector: 'app-model-testing', templateUrl: './model-testing.component.html', styleUrls: ['./model-testing.component.scss']})
export class ModelTestingComponent implements OnInit {
  @Input('trainingData')trainingData : [TrainingData];
  @Input('modelId')modelId : String;


  selectedTrainingData: String = null;
  paramTimeout = null
  testWord = "";
  testing = false;
  synonyms: [String] = null;
  
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
      if (element.lastTrainingEnd) {
        this.tdata.push({
          endTimeLong: element.lastTrainingEnd,
          endTime: d.toDateString() + ' ' + d.toLocaleTimeString(),
          url: element.modelUrl
        });
      }
    });
    this.sortByDate();
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
    
  }

  testModel() {
    this.testing = true;
    clearTimeout(this.paramTimeout);
    
    this.paramTimeout = setTimeout(() => {
      this.modelService.test(this.modelId, this.selectedTrainingData, this.testWord)
        .subscribe(data => {
          if (data.results.length < 1) {
            this.synonyms = ["none"];
          } else {
            this.synonyms = data.results;
          }
          this.testing = false
        })
    }, 500);
  }

  copyAndTestSynonym(event) {
    this.testWord = event.target.textContent;
    this.testModel();
  }
  
  sortByDate(): void {
    this.tdata.sort((a , b) => {
      return this.getTime(new Date(b.endTimeLong)) - this.getTime(new Date(a.endTimeLong));
    });
  }
  
  private getTime(date?: Date) {
    return date != null ? date.getTime() : 0;
  }

  
}
