import {Component, OnInit} from '@angular/core';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';

@Component({
  selector: 'app-diary-users',
  templateUrl: './diary-users.component.html',
  styleUrls: ['./diary-users.component.css']
})
export class DiaryUsersComponent implements OnInit {
  searchText;
  errorMessage: string = null;

  users: User[] = [];

  config = {
    id: 'custom',
    itemsPerPage: 20,
    currentPage: 1,
    totalItems: this.users.length
  };

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.findAll().subscribe(users => {
      this.users = users;
      this.users.sort((a, b) => (a.lastName.localeCompare(b.lastName)));
    });
  }

}
