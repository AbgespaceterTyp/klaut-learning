import { Component, OnInit, Input } from '@angular/core';
import { UserService } from '../../_services/user.service';
import { UserDto, OrganizationDto } from '../../_models';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  @Input('organization') organization: OrganizationDto;

  users: UserDto[];

  loadingUser = true;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.load().subscribe(content => {
      this.users = content;
      this.loadingUser = false;
    })
  }

}
