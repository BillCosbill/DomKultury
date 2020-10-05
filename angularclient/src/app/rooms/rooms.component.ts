import { Component, OnInit } from '@angular/core';
import {Room} from '../_model/room';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent implements OnInit {

  rooms: Room[] = [];

  base64Data: any;
  retrieveResonse: any;

  constructor(private roomService: RoomService, private imageService: ImageService, private authService: AuthService) {
  }

  ngOnInit() {
    this.roomService.findAll().subscribe(data => {
      this.rooms = data;
      data.forEach(x => {
        if (x.imageId != null){
          this.imageService.getImage(x.imageId)
            .subscribe(
              res => {
                this.retrieveResonse = res;
                this.base64Data = this.retrieveResonse.picByte;
                x.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
              }
            );
        }});
    });
  }

  gotImage(room: Room){
    return room.retrievedImage != null;
  }
}
