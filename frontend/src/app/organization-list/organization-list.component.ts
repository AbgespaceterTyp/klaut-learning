import { Component, OnInit } from '@angular/core';
import { OrganizationDto } from '../_models';
import { OrganizationService } from '../_services/index';

@Component({
  selector: 'app-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: ['./organization-list.component.scss']
})
export class OrganizationListComponent implements OnInit {

  organizations: OrganizationDto[];

  constructor(private organizationService: OrganizationService) { }

  ngOnInit() {
    this.organizationService.load().subscribe(organizations => {
      this.organizations = organizations.content;
    })
  }

}
