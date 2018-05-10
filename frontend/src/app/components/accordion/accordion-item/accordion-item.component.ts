import { Component, OnInit, Input } from '@angular/core';
import { Word2Vec } from '../../../_models';
import { ModelService } from '../../../_services';
import { Timeouts } from 'selenium-webdriver';
import { timeout } from 'q';

@Component({
  selector: 'app-accordion-item',
  templateUrl: './accordion-item.component.html',
  styleUrls: ['./accordion-item.component.scss']
})
export class AccordionItemComponent implements OnInit {
  @Input('model') model: Word2Vec;
  @Input('opend') opend: boolean;

  paramTimeout = null
  loading = false;

  constructor(private modelService: ModelService) { }

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
  }

  updateParams() {
    clearTimeout(this.paramTimeout);
    this.loading = true;

    this.paramTimeout = setTimeout(() => {
      this.modelService.updateParams(this.model.params, this.model.id)
      .subscribe(
        data => {
          this.loading = false;
          console.log(data);
        }
      );
    }, 500);
  }

}
