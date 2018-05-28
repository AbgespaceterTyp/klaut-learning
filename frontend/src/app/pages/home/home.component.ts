import { Component, OnInit, style } from '@angular/core';

import { ModelService } from '../../_services/index';
import { Word2Vec, ModelDto } from '../../_models/index';
import { enable, destroy } from 'splash-screen';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  models: ModelDto[];

  modelDto: ModelDto = {
    description: "",
    name: "",
    algorithm: "Word2Vec",
  }


  constructor(private modelService: ModelService) { }

  ngOnInit() {
    enable("windcatcher");
    this.modelService.load()
    .subscribe(data => {
      this.models = data.content; 
      destroy();
    });
  }

  addModel() {
    if (this.modelDto.name.length > 3) {
      this.modelService.create(this.modelDto)
      .subscribe(data => {
        let id = data as any;
        this.models.unshift({
          id: id.id,
          name: this.modelDto.name,
          algorithm: this.modelDto.algorithm,
          description: this.modelDto.description,
        })
      });
    }
  }
}