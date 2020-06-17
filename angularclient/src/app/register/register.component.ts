import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  dayArray: number[] = [31];
  monthArray: string[] = ['Styczeń','Luty','Marzec','Kwiecień','Maj','Czerwiec','Lipiec','Śierpień','Wrzesień','Październik','Listopad','Grudzień'];
  yearArray: number[] = [200];
  currentYear: number = (new Date).getFullYear();


  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService) { }

  ngOnInit() {
    for(let i=0; i<31; i++){
      this.dayArray[i] = i+1;
    }

    for(let i=0; i<200; i++){
      this.yearArray[i] = this.currentYear-i;
    }
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
