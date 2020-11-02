import { Component, OnInit } from '@angular/core';
import {Room} from '../_model/room';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';
import {AuthService} from '../_services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent implements OnInit {

  rooms: Room[] = [];

  base64Data: any;
  retrieveResonse: any;

  room: Room = new Room();
  selectedFile: File;
  message: string;
  uploadedImageId: any;

  roomIdToDelete: number = null;

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

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  onSubmit() {
    if (this.selectedFile !== undefined && this.selectedFile !== null) {
      const uploadImageData = new FormData();
      uploadImageData.append('file', this.selectedFile, this.selectedFile.name);

      this.imageService.saveImage(uploadImageData)
        .subscribe((response) => {
            let a = JSON.stringify(response.body);
            let b = JSON.parse(a);
            this.uploadedImageId = b.id;
            this.roomService.addRoom(this.room, this.uploadedImageId).subscribe(() => this.ngOnInit());
          }
        );
    } else {
      this.roomService.addRoom(this.room, null).subscribe(() => this.ngOnInit());
    }
  }

  selectRoomIdToDelete(id: number) {
    this.roomIdToDelete = id;
  }

  deleteRoom() {
    this.roomService.deleteRoom(this.roomIdToDelete).subscribe(() => this.ngOnInit());
  }
}
