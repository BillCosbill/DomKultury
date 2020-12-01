import { Component, OnInit } from '@angular/core';
import {EventSettingsModel, View} from '@syncfusion/ej2-angular-schedule';
import {DataSource} from '../_model/data-source';
import {User} from '../_model/user';
import {Subject} from '../_model/subject';
import {TokenStorageService} from '../_services/token-storage.service';
import {ValidationService} from '../_services/validation/validation.service';
import {LessonService} from '../_services/lesson.service';
import {UserService} from '../_services/user.service';
import {AuthService} from '../_services/auth.service';
import {SubjectService} from '../_services/subject.service';
import {ActivatedRoute} from '@angular/router';

declare var openErrorModal;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  errorMessage: string = null;

  public setView: View = 'Month';
  public weekFirstDay = 1;
  public eventObject: EventSettingsModel = {
    dataSource: []
  };

  scheduleData: DataSource[] = [];

  user: User = new User();
  userId: number;

  subjects: Subject[] = [];
  users: User[] = [];

  userEdited: User = new User();

  form: any = {};

  constructor(private token: TokenStorageService, private validationService: ValidationService, private tokenStorage: TokenStorageService, private lessonService: LessonService, private userService: UserService, public authService: AuthService) {
  }

  ngOnInit() {
    this.subjects = [];
    this.scheduleData = [];

    this.userService.getUser(this.token.getUser().id).subscribe(user => {
      this.user = user;
      this.userId = user.id;

      this.userService.getUserSubjects(this.userId).subscribe(subjects => {
        this.subjects = subjects;

        subjects.forEach(subject => {
          subject.lessonsId.forEach(lessonId => {
            this.lessonService.getLesson(lessonId).subscribe(lesson => {
              this.scheduleData.push(new DataSource(lesson.topic, lesson.startDate, lesson.finishDate));
            });
          });
        });
      });
    });

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });

    this.eventObject.dataSource = this.scheduleData;
  }

  loggedUsersProfile() {
    return this.userId === this.token.getUser().id;
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
    this.userEdited.firstName = this.user.firstName;
    this.userEdited.lastName = this.user.lastName;
    this.userEdited.email = this.user.email;
  }

  editUser() {
    this.userService.updateUser(this.userEdited, this.userId).subscribe(user => {

      // if edit yourself
      if (this.userId === this.tokenStorage.getUser().id) {
        const form = this.tokenStorage.getUser();
        form.email = user.email;
        this.tokenStorage.saveUser(form);
      }

      this.validationService.refreshData();
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  dataImportedIntoScheduler() {
    // @ts-ignore
    return this.eventObject.dataSource.length > 0;
  }

  hasAssignedSubjects() {
    return this.subjects.length > 0;
  }

  changePassword() {
    this.userService.changePassword(this.form, this.user.id).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
