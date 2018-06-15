import { Component } from '@angular/core';
import { AuthenticationService } from './_services';
import { Router } from '@angular/router';
import { destroy } from 'splash-screen';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';

  constructor(private authenticationService: AuthenticationService, private router: Router) {
    this.router.events.subscribe(data => {
      destroy();
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  loggedIn() {
    return this.authenticationService.loggedIn();
  }
}
