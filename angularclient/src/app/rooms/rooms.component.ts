import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent implements OnInit {

  collection3 = [
    {id:1, title: 'Pracownia komputerowa'},
    {id:2, title: 'Sala kinowa'},
    {id:3, title: 'Studio nagrań'},
    {id:4, title: 'Bar'},
    {id:5, title: 'Sala gimnastyczna'},
    {id:6, title: 'Sala plastyczna'},
    {id:7, title: 'Sala orkiestry'}
  ];

  constructor() {
  }


  ngOnInit() {
  }


}
