import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  model: any = {};
  loading = false;

  constructor(
      private router: Router) { }

  register() {
      this.loading = true;
      // this.userService.create(this.model)
      //     .subscribe(
      //         data => {
      //             this.router.navigate(['/login']);
      //         },
      //         error => {
      //             this.loading = false;
      //         });
  }
}
