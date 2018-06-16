import { Component } from '@angular/core';
import { CreateOrganizationDto } from '../../_models';
import { OrganizationService, AuthenticationService } from '../../_services';
import { Router } from '@angular/router';
import { AppComponent } from '../../app.component';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  model: CreateOrganizationDto = {};
  loading = false;

  constructor(private organizationService: OrganizationService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private appComponent: AppComponent) { }

  register() {
    this.loading = true;
    this.organizationService.create(this.model)
      .subscribe(
        data => {
          this.authenticationService.login(this.model.adminEmail,
            this.model.name, this.model.adminPassword)
            .subscribe(
              data => {
                this.appComponent.organization = this.model.name;
                this.router.navigate(['/']);
              },
              error => {
                this.loading = false;
              });
        },
        error => {
          this.loading = false;
        }
      )
  }
}
