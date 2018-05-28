import { Component } from '@angular/core';
import { AuthenticationService } from './_services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';

  constructor(private authenticationService: AuthenticationService, private router: Router) { }
  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
  loggedIn() {
    return this.authenticationService.loggedIn();
  }
}
