import { Component, OnInit, Input } from '@angular/core';
import { Word2Vec } from '../../../_models';

@Component({
  selector: 'app-accordion-item',
  templateUrl: './accordion-item.component.html',
  styleUrls: ['./accordion-item.component.scss']
})
export class AccordionItemComponent implements OnInit {
  @Input('model') model: Word2Vec;
  @Input('opend') opend: boolean;

  constructor() { }

  ngOnInit() {
    if(!this.model.params) {
      this.model.params = {
        iterations: 0,
        layerSize: 0,
        minWordFrequency: 0,
        seed: 0,
        windowSize: 0,
      }
    }
    console.log(this.model, this.opend);
    
  }

}
