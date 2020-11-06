import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../_services/token-storage.service';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';

declare var openErrorModal;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  errorMessage: string = null;

  user: User = new User();
  userEdited: User = new User();

  form: any = {};

  constructor(private token: TokenStorageService, private userService: UserService, private tokenStorage: TokenStorageService) {
  }

  ngOnInit() {
    this.userService.getUser(this.token.getUser().id).subscribe(user => {
      this.user = user;
    });
  }

  startEditing() {
    this.userEdited = new User();
    this.userEdited.id = this.user.id;
    this.userEdited.firstName = this.user.firstName;
    this.userEdited.lastName = this.user.lastName;
    this.userEdited.pesel = this.user.pesel;
    this.userEdited.email = this.user.email;
  }

  editProfile() {
    this.userService.updateUser(this.userEdited, this.user.id).subscribe(user => {

      const form = this.tokenStorage.getUser();
      form.email = user.email;
      this.tokenStorage.saveUser(form);

      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  changePassword() {
    this.userService.changePassword(this.form, this.user.id).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
