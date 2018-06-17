import { Component, OnInit } from '@angular/core';
import { AuthenticationService, LocalStorageService, OrganizationService } from './_services';
import { Router } from '@angular/router';
import { destroy } from 'splash-screen';
import { HttpEventType, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Klaut Learning';
  imageUrl: String;
  organization: String;

  progress: {
    percentage: number;
    uploaded: boolean;
  } = {
      percentage: 0,
      uploaded: true
    };

  constructor(private authenticationService: AuthenticationService, private router: Router,
    private localStorageService: LocalStorageService,
    private organizationService: OrganizationService) {

    this.router.events.subscribe(data => {
      destroy();
    });
  }

  ngOnInit() {
    // Hacky Hack to tell the browser that the uri has changed. Unfortunately prevents caching
    this.imageUrl = this.organizationService.getOrganizationImageUrl() + "?rerender=" + Math.floor(Math.random() * 100) + 1;
    this.organization = this.localStorageService.currentOrganization.name;
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  loggedIn() {
    return this.authenticationService.loggedIn();
  }

  uploadOrgaDummy() {
    document
      .getElementById('organizationImageInput')
      .click();
  }

  uploadOrganizationImage(event) {
    this.progress.percentage = 0;
    this.progress.uploaded = false;
    let files = event.target.files;
    if (files.length > 0) {
      this.organizationService.uploadFile(files[0])
        .subscribe(event => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress.percentage = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.progress.uploaded = true;
            this.ngOnInit()
          }
        })
    }
  }
}
