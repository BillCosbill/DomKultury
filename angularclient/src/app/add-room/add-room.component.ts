import {Component} from '@angular/core';
import {Room} from '../_model/room';
import {ActivatedRoute, Router} from '@angular/router';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrls: ['./add-room.component.css']
})
export class AddRoomComponent {

  room: Room;
  selectedFile: File;
  message: string;
  uploadedImageId: any;

  constructor(private rote: ActivatedRoute, private router: Router, private roomService: RoomService, private imageService: ImageService) {
    this.room = new Room();
  }

  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  seatsInRange() {
    return this.room.seats > 0 || this.room.seats == null;
  }

  onSubmit() {
    const uploadImageData = new FormData();
    uploadImageData.append('file', this.selectedFile, this.selectedFile.name);

    this.imageService.saveImage(uploadImageData)
      .subscribe((response) => {
          let a = JSON.stringify(response.body);
          let b = JSON.parse(a);
          this.uploadedImageId = b.id;
          this.roomService.addRoom(this.room, this.uploadedImageId).subscribe(() => this.goToRoomsList());
        }
      );
  }

  goToRoomsList() {
    this.router.navigate(['/rooms']);
  }
}
