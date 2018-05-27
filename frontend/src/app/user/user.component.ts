import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { UserDto } from '../_models';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  users: UserDto[];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.load().subscribe(pageable => {
      this.users = pageable.content;
    })
  }

}
