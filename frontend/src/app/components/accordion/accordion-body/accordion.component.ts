import { Component, OnInit, Input } from '@angular/core';
import { Word2Vec } from '../../../_models';

@Component({
  selector: 'app-accordion',
  templateUrl: './accordion.component.html',
  styleUrls: ['./accordion.component.scss']
})
export class AccordionComponent implements OnInit {
  @Input('models') models: [Word2Vec]

  constructor() { }

  ngOnInit() {
  }

}
