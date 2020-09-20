import {Component, OnInit} from '@angular/core';
import {User} from '../_model/user';
import {Room} from '../_model/room';
import {ActivatedRoute} from '@angular/router';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';
import {RoomService} from '../_services/room.service';
import {ImageService} from '../_services/image.service';

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
export class RoomDetailsComponent implements OnInit {

  roomId: number;
  room: Room = new Room();

  currentUser: any = null;
  userId: number = null;
  user: User = new User();

  base64Data: any;
  retrieveResonse: any;

  constructor(private roomService: RoomService, private route: ActivatedRoute, private tokenStorageService: TokenStorageService, private userService: UserService, private imageService: ImageService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.roomId = +params['id'];
    });

    this.currentUser = this.tokenStorageService.getUser();
    this.userId = this.currentUser.id;

    this.userService.getUser(this.userId).subscribe(result => {
      this.user = result;
    });

    this.refreshData();
  }

  private refreshData() {
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
}
