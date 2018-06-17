import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { UserService } from '../../_services/user.service';
import { UserDto, OrganizationDto } from '../../_models';

@Component({
  selector: 'app-user',

  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  @Input('organization') organization: OrganizationDto;
  @Output() loading = new EventEmitter<boolean>();

  users: UserDto[];
  newUser = new UserDto();

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.loadUsers();
  }

  deleteUser(email: string) {
    this.loading.emit(true);
    this.userService.deleteUser(email).subscribe(response => {
      this.loadUsers()
    })
  }

  loadUsers() {
    this.loading.emit(true);
    this.userService.load().subscribe(content => {
      this.loading.emit(false);
      this.users = content;
    })
  }

  createUser() {
    this.loading.emit(true);
    this.userService.create(this.newUser).subscribe(response => {
      this.newUser = new UserDto();
      this.loadUsers()
    });
  }
}
