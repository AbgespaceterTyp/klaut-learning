import { Component, OnInit, style } from '@angular/core';

import { ModelService } from '../_services/index';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private modelServoce: ModelService) { }

  ngOnInit() {
    console.log(this.modelServoce.train()
    .subscribe(data => {
      console.log(data);
      })
    );
    
  }

}