import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';
import {Lesson} from '../_model/lesson';
import {LessonService} from '../_services/lesson.service';
import {Room} from '../_model/room';
import {RoomService} from '../_services/room.service';
import {AuthService} from '../_services/auth.service';
import {ValidationService} from '../_services/validation/validation.service';
import {TokenStorageService} from '../_services/token-storage.service';

declare var openErrorModal;

@Component({
  selector: 'app-subject-details',
  templateUrl: './subject-details.component.html',
  styleUrls: ['./subject-details.component.css']
})
export class SubjectDetailsComponent implements OnInit {
  errorMessage: string = null;

  subjectId: number;
  subject: Subject = new Subject();

  teacher: User = new User();
  lessons: Lesson[] = [];
  users: User[] = [];
  rooms: Room[] = [];

  subjectEdited: Subject = new Subject();
  newLesson: Lesson = new Lesson();
  lessontIdToDelete: number = null;

  startDate: string = null;
  startTime: string = null;
  finishTime: string = null;

  constructor(public validationService: ValidationService, public authService: AuthService, private subjectService: SubjectService,
              private roomService: RoomService, private route: ActivatedRoute, private userService: UserService,
              private lessonService: LessonService, private token: TokenStorageService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.subjectId = +params['id'];
    });

    this.refreshData();
  }

  private refreshData() {
    this.subjectService.getSubject(this.subjectId).subscribe(result => {
      this.subject = result;

      this.userService.getUser(this.subject.teacherId).subscribe(user => {
        this.teacher = user;
      });

      this.lessons = [];
      this.subject.lessonsId.forEach(x => {
        this.lessonService.getLesson(x).subscribe(lesson => {
          this.lessons.push(lesson);
          this.lessons.sort((a, b) => (a.startDate.localeCompare(b.finishDate)));
        });
      });
    });

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });

    this.roomService.findAll().subscribe(rooms => {
      this.rooms = rooms;
    });
  }

  onSubmit() {
    this.subjectService.updateSubject(this.subjectEdited, this.subject.id).subscribe(() => this.refreshData(), error => {
      this.createErrorModal(error.error.message);
    });
  }

  startEditing() {
    this.subjectEdited = new Subject();
    this.subjectEdited.id = this.subject.id;
    this.subjectEdited.name = this.subject.name;
    this.subjectEdited.description = this.subject.description;
  }

  addLesson() {
    this.newLesson.startDate = this.startDate + 'T' + this.startTime + ':00';
    this.newLesson.finishDate = this.startDate + 'T' + this.finishTime + ':00';
    this.newLesson.subjectId = this.subjectId;

    this.lessonService.addLesson(this.newLesson).subscribe(() => {
      this.newLesson = new Lesson();
      this.startDate = null;
      this.startTime = null;
      this.finishTime = null;
      this.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  timeIncorrect() {
    return this.finishTime <= this.startTime;
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
      this.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  selectLessonIdToDelete(id: number) {
    this.lessontIdToDelete = id;
  }

  userIsOwner() {
    return this.token.getUser().id === this.subject.teacherId || this.authService.isAdminLoggedIn()
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
