import { Component, OnInit } from '@angular/core';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';
import {Router} from '@angular/router';
import {Role} from '../_model/role';

@Component({
  selector: 'app-administrator-panel-activations',
  templateUrl: './administrator-panel-activations.component.html',
  styleUrls: ['./administrator-panel-activations.component.css']
})
export class AdministratorPanelActivationsComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.refreshData();
  }

  makePrettyRoleName(name: string) {
    return name.substr(5).toLowerCase();
  }

  private refreshData() {
    this.userService.findAll().subscribe(data => {
      this.users = [];
      data.forEach(user => {
        if (!user.enable) {
          this.users.push(user);
        }
      });
    });
  }

  activate(id: number) {
    this.userService.activeAccount(id).subscribe(() => this.refreshData());
  }

  checkIfEnable(user: User) {
    if (user.enable) {
      return 'Aktywny';
    } else {
      return 'Nieaktywny';
    }
  }

}
