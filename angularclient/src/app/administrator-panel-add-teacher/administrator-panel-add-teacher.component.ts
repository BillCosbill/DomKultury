import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services/auth.service';

declare var openErrorModal;

@Component({
  selector: 'app-administrator-panel-add-teacher',
  templateUrl: './administrator-panel-add-teacher.component.html',
  styleUrls: ['./administrator-panel-add-teacher.component.css']
})
export class AdministratorPanelAddTeacherComponent implements OnInit {
  errorMessage: string = null;

  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
  }

  onSubmit() {
    this.authService.register(this.form).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      err => {
        this.isSignUpFailed = true;
        this.createErrorModal(err.error.message);
      }
    );
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
