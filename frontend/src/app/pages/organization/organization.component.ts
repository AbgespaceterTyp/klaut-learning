import { Component, OnInit } from '@angular/core';
import { OrganizationDto } from '../../_models';
import { OrganizationService, LocalStorageService } from '../../_services';
import { Subscription } from '../../_models/subscription';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy } from '@angular/compiler/src/core';

@Component({
  selector: 'app-organization',


  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {
  organization: OrganizationDto;
  subscription: Subscription;
  loadingSubscription = false;
  subscriptionLevel = "COPPER";

  organizationImageUrl = this.organizationService.getOrganizationImageUrl();

  uploadingFile = false;
  loading = false;

  progress: {
    percentage: number;
    uploaded: boolean;
  } = {
      percentage: 0,
      uploaded: true
    };
  constructor(private organizationService: OrganizationService, private localStorageService: LocalStorageService) { }

  ngOnInit() {
    this.organization = this.localStorageService.currentOrganization;
    this.loadingSubscription = true;
    this.organizationService.getSubscription()
      .subscribe(data => {
        this.subscription = data;
        this.loadingSubscription = false;
      })
  }

  uploadDummy() {
    document
      .getElementById('fileInput')
      .click();
  }

  uploadFile(event) {
    this.uploadingFile = true;
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
            this.uploadingFile = false;
          }
        })
    }
  }
  onLoading(loading: boolean) {
    setTimeout(() => {
      this.loading = loading;
    });
  }

  renewSubscription() {
    this.organizationService.renewSubscription(this.subscriptionLevel)
    .subscribe(data => {
      console.log(data);
    });
  }
}
