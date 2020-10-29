import {Component, OnInit} from '@angular/core';
import {Room} from '../_model/room';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../_services/user.service';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';
import {Lesson} from '../_model/lesson';
import {EventSettingsModel, View} from '@syncfusion/ej2-angular-schedule';
import {DataSource} from '../_model/data-source';

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

  base64Data: any;
  retrieveResonse: any;

  constructor(private roomService: RoomService, private route: ActivatedRoute, private userService: UserService, private imageService: ImageService) {
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
}
