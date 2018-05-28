import { Component, OnInit } from '@angular/core';
import { OrganizationDto } from '../../_models';
import { OrganizationService, LocalStorageService } from '../../_services';

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {
  organization: OrganizationDto;

  constructor(private organizationService: OrganizationService, private localStorageService: LocalStorageService) { }

  ngOnInit() {
    this.organization = this.localStorageService.currentOrganization;
  }

}
