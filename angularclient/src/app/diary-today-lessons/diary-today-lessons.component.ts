import {Component, OnInit} from '@angular/core';
import {formatDate} from '@angular/common';
import {LessonService} from '../_services/lesson.service';
import {Lesson} from '../_model/lesson';
import {TokenStorageService} from '../_services/token-storage.service';
import {SubjectService} from '../_services/subject.service';
import {AuthService} from '../_services/auth.service';
import {Room} from '../_model/room';
import {RoomService} from '../_services/room.service';

@Component({
  selector: 'app-diary-today-lessons',
  templateUrl: './diary-today-lessons.component.html',
  styleUrls: ['./diary-today-lessons.component.css']
})
export class DiaryTodayLessonsComponent implements OnInit {

  lessons: Lesson[] = [];
  rooms: Room[] = [];

  userId: number = null;

  constructor(private roomService: RoomService, private authService: AuthService, private lessonService: LessonService, private tokenStorageService: TokenStorageService, private subjectService: SubjectService) {
  }

  ngOnInit() {
    if (!!this.tokenStorageService.getToken()) {
      const user = this.tokenStorageService.getUser();
      this.userId = user.id;
    }

    this.lessons = [];
    const dateNow = formatDate(new Date(), 'yyyy-MM-dd', 'en-US');

    this.lessonService.findAll().subscribe(lessons => {
      lessons.forEach(lesson => {
        this.subjectService.getSubject(lesson.subjectId).subscribe(subject => {
          if (dateNow === lesson.startDate.substr(0, 10) && this.userId === subject.teacherId) {
            this.lessons.push(lesson);
          }
        });
      });
    });

    this.roomService.findAll().subscribe(rooms => {
      this.rooms = rooms;
    });
  }

  formatDateTime(datetime: string) {
    return datetime.replace('T', ' ');
  }

  getRoomData(roomId: number) {
    let room: Room = new Room();

    this.rooms.forEach(x => {
      if (x.id === roomId) {
        room = x;
      }
    });

    return room.number + ' - ' + room.destiny;
  }

  deleteLesson(id: number) {
    this.lessonService.deleteLesson(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
