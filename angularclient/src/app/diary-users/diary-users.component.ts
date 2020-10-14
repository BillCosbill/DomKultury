import {Component, OnInit} from '@angular/core';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-diary-users',
  templateUrl: './diary-users.component.html',
  styleUrls: ['./diary-users.component.css']
})
export class DiaryUsersComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.findAll().subscribe(users => {
      this.users = users;
    });
  }

}
