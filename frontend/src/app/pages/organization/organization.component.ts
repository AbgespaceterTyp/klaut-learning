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

  loading = false;

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

  onLoading(loading: boolean) {
    setTimeout(() => {
      this.loading = loading;
    });
  }

  renewSubscription() {
    this.organizationService.renewSubscription(this.subscriptionLevel)
      .subscribe(data => {
        this.subscription = data;
      });
  }
}
