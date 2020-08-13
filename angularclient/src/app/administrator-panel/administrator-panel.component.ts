import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';
import {Router} from '@angular/router';
import {Role} from '../_model/role';

@Component({
  selector: 'app-administrator-panel',
  templateUrl: './administrator-panel.component.html',
  styleUrls: ['./administrator-panel.component.css']
})
export class AdministratorPanelComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.refreshData();
  }

  makePrettyRoleName(name: string) {
    return name.substr(5).toLowerCase();
  }

  checkIfContaintsRole(roles: Role[], role: string) {
    return roles[0].name === role;
  }

  delete(id: number) {
    this.userService.deleteUser(id).subscribe(() => this.refreshData());
  }

  private refreshData() {
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  changeRole(id: number, role: string) {
    this.userService.changeRole(role, id).subscribe(() => this.refreshData());
  }
}
