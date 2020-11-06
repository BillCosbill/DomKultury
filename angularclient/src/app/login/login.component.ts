import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services/auth.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';

declare var openErrorModal;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  errorMessage: string = null;

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessagee = '';
  roles: string[] = [];

  emailToSendNewPassword: string = null;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService, private userService: UserService) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  onSubmit() {
    this.authService.login(this.form).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.reloadPage();
      },
      err => {
        this.errorMessagee = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage() {
    window.location.reload();
  }

  sendNewPassword() {
    this.userService.generateNewPassword(this.emailToSendNewPassword).subscribe(() => {
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
