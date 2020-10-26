import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-administrator-panel-add-teacher',
  templateUrl: './administrator-panel-add-teacher.component.html',
  styleUrls: ['./administrator-panel-add-teacher.component.css']
})
export class AdministratorPanelAddTeacherComponent implements OnInit {

  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

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
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }

}
