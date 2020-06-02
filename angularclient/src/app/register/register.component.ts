import { Component, OnInit } from '@angular/core';

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

  constructor() { }

  ngOnInit() {
    for(let i=0; i<31; i++){
      this.dayArray[i] = i+1;
    }

    for(let i=0; i<200; i++){
      this.yearArray[i] = this.currentYear-i;
    }
  }

}
