import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AuthenticationService, LocalStorageService } from '../../_services/index';
import { AppComponent } from '../../app.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  model: any = {};
  loading = false;
  returnUrl: string;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private authenticationService: AuthenticationService,
      private appComponent: AppComponent,
      private localStorageService: LocalStorageService) { }

  ngOnInit() {
      // get return url from route parameters or default to '/'
      this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';      
  }

  login() {
      this.loading = true;
      this.authenticationService.login(this.model.username, this.model.organization, this.model.password)
          .subscribe(
              data => {
                  this.appComponent.organization = this.localStorageService.currentOrganization.name
                  this.appComponent.imageUrl = `api/${this.localStorageService.currentOrganization.key}/image`;
                  this.router.navigate([this.returnUrl]);
              },
              error => {
                  this.loading = false;
              });

  }
}
