import {Component, OnInit} from '@angular/core';
import {Lesson} from '../_model/lesson';
import {LessonService} from '../_services/lesson.service';
import {ActivatedRoute} from '@angular/router';
import {Room} from '../_model/room';
import {RoomService} from '../_services/room.service';
import {AuthService} from '../_services/auth.service';

declare var openErrorModal;

@Component({
  selector: 'app-lesson-details',
  templateUrl: './lesson-details.component.html',
  styleUrls: ['./lesson-details.component.css']
})
export class LessonDetailsComponent implements OnInit {
  errorMessage: string = null;

  lessonId: number;
  subjectId: number;
  lesson: Lesson = new Lesson();
  room: Room = new Room();
  rooms: Room[] = [];
  startDate: string;
  startTime: string;
  finishTime: string;

  editedLesson: Lesson = new Lesson();
  editedStartDate: string;
  editedStartTime: string;
  editedFinishTime: string;

  constructor(private lessonService: LessonService, private roomService: RoomService, private route: ActivatedRoute, public authService: AuthService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.lessonId = +params.id;
      this.subjectId = +params.id2;
    });

    this.lessonService.getLesson(this.lessonId).subscribe(lesson => {
      this.lesson = lesson;

      this.startDate = lesson.startDate.substr(0, 10);
      this.startTime = lesson.startDate.substr(11, 8);
      this.finishTime = lesson.finishDate.substr(11, 8);

      this.roomService.getRoom(this.lesson.roomId).subscribe(room => {
        this.room = room;
      });
    });

    this.roomService.findAll().subscribe(rooms => {
      this.rooms = rooms;
    });
  }


  onSubmit() {
    this.lessonService.updateLesson(this.editedLesson, this.lessonId).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  changeDatetime() {
    if (this.editedStartTime.length === 5) {
      this.editedStartTime = this.editedStartTime + ':00';
    }
    if (this.editedFinishTime.length === 5) {
      this.editedFinishTime = this.editedFinishTime + ':00';
    }

    this.editedLesson.startDate = this.editedStartDate + 'T' + this.editedStartTime;
    this.editedLesson.finishDate = this.editedStartDate + 'T' + this.editedFinishTime;
    this.lessonService.updateLesson(this.editedLesson, this.lessonId).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  startEditing() {
    this.editedLesson = new Lesson();
    this.editedLesson.id = this.lesson.id;
    this.editedLesson.topic = this.lesson.topic;
    this.editedLesson.description = this.lesson.description;
    this.editedLesson.startDate = this.lesson.startDate;
    this.editedLesson.finishDate = this.lesson.finishDate;
    this.editedLesson.roomId = this.lesson.roomId;
  }

  startEditingDatetime() {
    this.editedLesson = new Lesson();
    this.editedLesson.id = this.lesson.id;

    this.editedStartDate = this.startDate;
    this.editedStartTime = this.startTime;
    this.editedFinishTime = this.finishTime;
  }

  timeIncorrect() {
    return this.editedFinishTime <= this.editedStartTime;
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
