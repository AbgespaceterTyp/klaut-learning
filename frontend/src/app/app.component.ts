import { Component } from '@angular/core';
import { AuthenticationService, LocalStorageService, OrganizationService } from './_services';
import { Router } from '@angular/router';
import { destroy } from 'splash-screen';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Klaut Learning';
  imageUrl = this.organizationService.getOrganizationImageUrl();
  organization = this.localStorageService.currentOrganization.name;

  constructor(private authenticationService: AuthenticationService, private router: Router,
    private localStorageService: LocalStorageService,
    private organizationService: OrganizationService) {

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
