import { Component, OnInit } from '@angular/core';
import {EventService} from '../_services/event.service';
import {Event} from '../_model/event';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[] = [];

  constructor(private eventService: EventService) {
  }


  ngOnInit() {
    //TODO Formatowanie daty pobieranej z Eventów
    this.eventService.findAll().subscribe(data => {
      this.events = data;
    });
  }

}
