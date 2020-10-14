import {Component, OnInit} from '@angular/core';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';
import {AuthService} from '../_services/auth.service';
import {SubjectService} from '../_services/subject.service';
import {Subject} from '../_model/subject';
import {ActivatedRoute} from '@angular/router';
import {EventSettingsModel, View} from '@syncfusion/ej2-angular-schedule';
import {DataSource} from '../_model/data-source';
import {LessonService} from '../_services/lesson.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  public setView: View = 'Month';
  public weekFirstDay = 1;
  public eventObject: EventSettingsModel = {
    dataSource: [{}]
  };

  scheduleData: DataSource[] = [];

  user: User = new User();
  userId: number;

  subjects: Subject[] = [];
  users: User[] = [];

  userEdited: User = new User();

  constructor(private lessonService: LessonService, private userService: UserService, private authService: AuthService, private subjectService: SubjectService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
    });

    this.userService.getUser(this.userId).subscribe(user => {
      this.user = user;
    });

    this.subjects = [];
    this.scheduleData = [];
    this.subjectService.findAll().subscribe(subjects => {
      subjects.forEach(subject => {
        if (subject.teacherId === this.userId) {
          this.subjects.push(subject);

          subject.lessonsId.forEach(lessonId => {
            this.lessonService.getLesson(lessonId).subscribe(lesson => {
              this.scheduleData.push(new DataSource(lesson.topic, lesson.startDate, lesson.finishDate));
            });
          });
        }
      });
    });

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });

    this.eventObject.dataSource = this.scheduleData;
  }

  getTeacher(teacherId: number) {
    let user: User = new User();

    this.users.forEach(x => {
      if (x.id === teacherId) {
        user = x;
      }
    });

    return user.firstName + ' ' + user.lastName;
  }

  startEditing() {
    this.userEdited = new User();
    this.userEdited.id = this.user.id;
    this.userEdited.username = this.user.username;
    this.userEdited.firstName = this.user.firstName;
    this.userEdited.lastName = this.user.lastName;
    this.userEdited.pesel = this.user.pesel;
    this.userEdited.email = this.user.email;
  }

  editUser() {
    this.userService.updateUser(this.userEdited, this.userId).subscribe(() => {
      this.ngOnInit();
    });
  }
}
