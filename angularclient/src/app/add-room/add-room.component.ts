import { Component, OnInit } from '@angular/core';
import {Room} from '../_model/room';
import {ActivatedRoute, Router} from '@angular/router';
import {RoomService} from '../_services/room.service';

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrls: ['./add-room.component.css']
})
export class AddRoomComponent  {

  room: Room;

  constructor(private rote: ActivatedRoute, private router: Router, private roomService: RoomService) {
    this.room = new Room();
  }

  onSubmit() {
    this.roomService.addRoom(this.room).subscribe(() => this.goToBooksList());
  }

  goToBooksList() {
    this.router.navigate(['/home']);
  }

}
