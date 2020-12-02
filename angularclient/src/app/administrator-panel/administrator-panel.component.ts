import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';
import {Role} from '../_model/role';
import {ValidationService} from '../_services/validation/validation.service';

declare var openErrorModal;

@Component({
  selector: 'app-administrator-panel',
  templateUrl: './administrator-panel.component.html',
  styleUrls: ['./administrator-panel.component.css']
})
export class AdministratorPanelComponent implements OnInit {
  searchText;

  users: User[] = [];
  selectUserId: number = null;
  selectRole: string = null;

  userIdToResetPassword: number = null;
  userIdToDelete: number = null;
  errorMessage: string = null;

  config = {
    id: 'custom',
    itemsPerPage: 20,
    currentPage: 1,
    totalItems: this.users.length
  };

  constructor(private userService: UserService, private validationService: ValidationService) {
  }

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      this.users = [];
      data.forEach(user => {
        if (user.enable) {
          this.users.push(user);
        }
      });
      this.users.sort((a, b) => (a.lastName.localeCompare(b.lastName)));
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

  deleteUser() {
    this.userService.deleteUser(this.userIdToDelete).subscribe(() => {
      this.ngOnInit();
      this.validationService.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }

  selectUserToResetPassword(id: any) {
    this.userIdToResetPassword = id;
  }

  resetPassword() {
    this.userService.generateNewPassword(this.userIdToResetPassword).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }
}

