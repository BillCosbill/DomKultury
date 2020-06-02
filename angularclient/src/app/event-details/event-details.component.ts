import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {

  collection2 = [
    {id:1, title:'Kurs programowania dla początkujących', data:'07.01.2020'},
    {id:2, title:'Tańce z maluchami', data:'12.05.2020'},
    {id:3, title:'Deszczowa piosenka - spektakl', data:'07.12.2020'},
    {id:4, title:'Kurs szydełkowania dla zaawansowanych', data:'21.05.2020'},
    {id:5, title:'Koncert BigBandu - "BigBandziory', data:'05.05.2020'},
    {id:6, title:'Konkurs piosenki dla przedszkolaków', data:'30.06.2020'},
    {id:7, title:'Kurs angielskiego - lekcja 1', data:'02.02.2020'},
    {id:8, title:'Zumba', data:'04.10.2020'},
    {id:9, title:'Zajęcia komputerowe dla seniorów', data:'17.04.2020'},
    {id:10, title:'Kurs angielskiego - lekcja 2', data:'09.02.2020'},
    {id:11, title:'Kurs programowania dla zaawansowanych', data:'25.03.2020'},
    {id:12, title:'Kurs programowania dla początkujących', data:'07.01.2020'},
    {id:13, title:'Tańce z maluchami', data:'12.05.2020'},
    {id:14, title:'Deszczowa piosenka - spektakl', data:'07.12.2020'},
    {id:15, title:'Kurs szydełkowania dla zaawansowanych', data:'21.05.2020'},
    {id:16, title:'Koncert BigBandu - "BigBandziory', data:'05.05.2020'},
    {id:17, title:'Konkurs piosenki dla przedszkolaków', data:'30.06.2020'},
    {id:18, title:'Kurs angielskiego - lekcja 1', data:'02.02.2020'},
    {id:19, title:'Zumba', data:'04.10.2020'},
    {id:20, title:'Zajęcia komputerowe dla seniorów', data:'17.04.2020'},
    {id:21, title:'Kurs angielskiego - lekcja 2', data:'09.02.2020'},
    {id:22, title:'Kurs programowania dla zaawansowanych', data:'25.03.2020'}
  ];

  eventId: number;

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.eventId = +params['id'];
    });
  }

}
