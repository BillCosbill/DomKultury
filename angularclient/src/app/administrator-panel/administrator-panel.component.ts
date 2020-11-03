import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';
import {Role} from '../_model/role';

declare var openErrorModal;

@Component({
  selector: 'app-administrator-panel',
  templateUrl: './administrator-panel.component.html',
  styleUrls: ['./administrator-panel.component.css']
})
export class AdministratorPanelComponent implements OnInit {

  users: User[] = [];
  selectUserId: number = null;
  selectRole: string = null;

  userIdToDelete: number = null;
  errorMessage: string = null;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      this.users = [];
      data.forEach(user => {
        if (user.enable) {
          this.users.push(user);
        }
      });
    });
  }

  makePrettyRoleName(name: string) {
    return name.substr(5).toLowerCase();
  }

  checkIfContaintsRole(roles: Role[], role: string) {
    return roles[0].name === role;
  }

  changeRole(id: number, role: string) {
    this.userService.changeRole(role, id).subscribe(() => this.ngOnInit(), error => {
      this.createErrorModal(error.error.message);
    });
  }

  selectUserToChangeRole(id: number) {
    this.selectUserId = id;
  }

  selectUserIdToDelete(id: number) {
    this.userIdToDelete = id;
  }

  // TODO modal jakiś ładny z komunikatem o błędzie

  deleteUser() {
    this.userService.deleteUser(this.userIdToDelete).subscribe(() => this.ngOnInit(), error => {
      this.createErrorModal(error.error.message);
    });
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}

