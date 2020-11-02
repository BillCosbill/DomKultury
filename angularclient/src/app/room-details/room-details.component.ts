import {Component, OnInit} from '@angular/core';
import {Room} from '../_model/room';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../_services/user.service';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';
import {EventSettingsModel, View} from '@syncfusion/ej2-angular-schedule';
import {DataSource} from '../_model/data-source';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
export class RoomDetailsComponent implements OnInit {

  public setView: View = 'Month';
  public weekFirstDay = 1;
  public eventObject: EventSettingsModel = {
    dataSource: []
  };

  scheduleData: DataSource[] = [];

  roomId: number;
  room: Room = new Room();
  roomEdited: Room = new Room();

  base64Data: any;
  retrieveResonse: any;

  selectedFile: File;
  uploadedImageId: any;

  constructor(private roomService: RoomService, private route: ActivatedRoute, private userService: UserService, private imageService: ImageService, private router: Router, private authService: AuthService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.roomId = +params['id'];

      this.roomService.getRoomLessons(this.roomId).subscribe(lessons => {
        lessons.forEach(lesson => {
          this.scheduleData.push(new DataSource(lesson.topic, lesson.startDate, lesson.finishDate));
        });
      });
    });

    this.eventObject.dataSource = this.scheduleData;

    this.roomService.getRoom(this.roomId).subscribe(result => {
      this.room = result;
      if (this.room.imageId != null) {
        this.imageService.getImage(this.room.imageId)
          .subscribe(
            res => {
              this.retrieveResonse = res;
              this.base64Data = this.retrieveResonse.picByte;
              this.room.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
            }
          );
      }
    });
  }

  gotImage() {
    return this.room.retrievedImage != null;
  }

  dataImportedIntoScheduler() {
    // @ts-ignore
    return this.eventObject.dataSource.length > 0;
  }

  deleteRoom() {
    this.roomService.deleteRoom(this.roomId).subscribe(() => this.goToRoomsList());
  }

  goToRoomsList() {
    this.router.navigate(['/rooms']);
  }

  startEditing() {
    this.roomEdited = new Room();
    this.roomEdited.id = this.room.id;
    this.roomEdited.number = this.room.number;
    this.roomEdited.destiny = this.room.destiny;
    this.roomEdited.description = this.room.description;
    this.roomEdited.imageId = this.room.imageId;
  }

  editRoom() {
    if (this.selectedFile !== undefined && this.selectedFile !== null) {
      const uploadImageData = new FormData();
      uploadImageData.append('file', this.selectedFile, this.selectedFile.name);

      this.imageService.saveImage(uploadImageData)
        .subscribe((response) => {
            let a = JSON.stringify(response.body);
            let b = JSON.parse(a);
            this.uploadedImageId = b.id;
            this.roomService.updateRoom(this.roomEdited, this.room.id, this.uploadedImageId).subscribe(() => this.ngOnInit());
          }
        );
    } else {
      this.roomService.updateRoom(this.roomEdited, this.room.id, null).subscribe(() => this.ngOnInit());
    }

    // this.roomService.updateRoom(this.roomEdited, this.room.id).subscribe(() => this.ngOnInit());
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  deleteImage() {
    this.roomService.deleteImageFromRoom(this.roomId).subscribe(() => this.ngOnInit());
  }
}
