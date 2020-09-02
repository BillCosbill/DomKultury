import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../_services/event.service';
import {Event} from '../_model/event';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {

  eventId: number;
  event: Event = new Event();

  currentUser: any = null;
  userId: number = null;
  user: User = new User();

  constructor(private eventService: EventService, private route: ActivatedRoute, private tokenStorageService: TokenStorageService, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.eventId = +params['id'];
    });

    this.currentUser = this.tokenStorageService.getUser();
    this.userId = this.currentUser.id;

    this.userService.getUser(this.userId).subscribe(result => {
      this.user = result;
    });

    this.refreshData();
  }

  addUser(event: Event, userId: number, eventId: number) {
    this.eventService.addUserToEvent(event, userId, eventId).subscribe(() => {
      this.refreshData();
    });
  }

  deleteUser(event: Event, userId: number, eventId: number) {
    this.eventService.deleteUserFromEvent(event, userId, eventId).subscribe(() => {
      this.refreshData();
    });
  }

  private refreshData() {
    this.eventService.getEvent(this.eventId).subscribe(result => {
      this.event = result;
    });
  }

  isSignedUpForEvent(event: Event, userId: number) {
    if (event.studentsId !== undefined) {
      return event.studentsId.includes(userId);
    }
  }

}
