import {Component, OnInit, Input} from '@angular/core';
import {Word2Vec} from '../../../_models';
import {ModelService} from '../../../_services';
import {Timeouts} from 'selenium-webdriver';
import {timeout} from 'q';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';


@Component({selector: 'app-accordion-item', templateUrl: './accordion-item.component.html', styleUrls: ['./accordion-item.component.scss']})
export class AccordionItemComponent implements OnInit {
  @Input('model')model : Word2Vec;
  @Input('opend')opend : boolean;

  paramTimeout = null
  loadingParams = false;
  loadingModel = false;
  faCoffee = faCoffee;

  loadingDesc = false;

  deleted = false;
  deleting = false;

  startTraining = false;

  uploadingFile = false;

  progress : {
    percentage: number;
    uploaded: boolean;
  } = {
    percentage: 0,
    uploaded: true
  };

  constructor(private modelService : ModelService) {}

  ngOnInit() {
    if (!this.model.params) {
      this.model.params = {
        iterations: 0,
        layerSize: 0,
        minWordFrequency: 0,
        seed: 0,
        windowSize: 0
      }
    }
  }

  updateModel() {
    this.loadingModel = true;
    this.modelService.loadModel(this.model.id)
      .subscribe(
        data => {
          console.log(data);
          this.loadingModel = false;
      });
  }

  updateParams() {
    clearTimeout(this.paramTimeout);
    this.loadingParams = true;

    this.paramTimeout = setTimeout(() => {
      this
        .modelService
        .updateParams(this.model.params, this.model.id)
        .subscribe(data => {
          this.loadingParams = false;
        });
    }, 500);
  }

  updateModelText() {
    clearTimeout(this.paramTimeout);
    this.loadingDesc = true;

    this.paramTimeout = setTimeout(() => {
      this
        .modelService
        .update(this.model, this.model.id)
        .subscribe(data => {
          this.loadingDesc = false;
        });
    }, 500);

  }

  delete() {
    this.deleting = true;
    this
      .modelService
      .delete(this.model.id)
      .subscribe(data => {
        this.deleted = true;
        this.deleting = false;
        console.log(data);
      })
  }

  uploadDummy() {
    document
      .getElementById(`uploadFile${this.model.id}`)
      .click();
  }

  uploadFile(event) {
    this.uploadingFile = true;
    this.progress.percentage = 0;
    this.progress.uploaded = false;
    let files = event.target.files;
    if (files.length > 0) {
      this
        .modelService
        .uploadFile(files[0], this.model.id)
        .subscribe(event => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress.percentage = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.progress.uploaded = true;
            this.uploadingFile = false;
            this.model.sourceUrl = "please relaod";
            console.log('File is completely uploaded!');
          }
        })
    }
  }

  train() {
    this.startTraining = true;
    this
      .modelService
      .train(this.model.id)
      .subscribe(data => {
        this.model.training = true;
        this.startTraining = false;
      });
  }

  trainable() : boolean {
    for(let key of Object.keys(this.model.params)) {
      if (this.model.params[key] < 1) {
        return false;
      }
    }
    return true;
  }

 

}
