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

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.load().subscribe(pageable => {
      this.users = pageable.content;
    })
  }

}
