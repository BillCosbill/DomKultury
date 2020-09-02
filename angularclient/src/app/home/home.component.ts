import { Component, OnInit } from '@angular/core';
import {EventService} from '../_services/event.service';
import {Event} from '../_model/event';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  //TODO dodać do obiektu wydarzenie pole z krótkim opisem
  //TODO pobierając z bazy ustawić limit na 20 wydarzeń (musi być podzielne na 4 żeby na stronie się zmieściło ładnie

  //TODO przypadek kiedy jest w bazie mniej eventów niż 4 !?!?!?!?!
  config = {
    id: 'custom',
    itemsPerPage: 4,
    currentPage: 1,
    totalItems: 0
  };

  events: Event[] = [];

  constructor(private eventService: EventService) {
  }


  ngOnInit() {
    //TODO Formatowanie daty pobieranej z Eventów
    this.eventService.findAll().subscribe(data => {
      this.events = data;
    });

    this.config.totalItems = this.events.length;
  }
}
