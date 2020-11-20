import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {User} from '../_model/user';
import {TokenStorageService} from '../_services/token-storage.service';
import {AuthService} from '../_services/auth.service';
import {UserService} from '../_services/user.service';

declare var openErrorModal;

@Component({
  selector: 'app-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.css']
})
export class DiaryComponent implements OnInit {
  errorMessage: string = null;

  subjects: Subject[] = [];
  currentUser: User;
  users: User[] = [];

  subjectIdToDelete: number = null;

  constructor(private subjectService: SubjectService, private token: TokenStorageService, public authService: AuthService, private userService: UserService) {
  }

  ngOnInit() {
    this.currentUser = this.token.getUser();

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });

    this.subjects = [];

    this.subjectService.findAll().subscribe(data => {
      data.forEach(subject => {
        if (subject.teacherId === this.currentUser.id) {
          this.subjects.push(subject);
          this.subjects.sort((a, b) => (a.name.localeCompare(b.name)));
        }
      });
    });
  }

  selectSubjectIdToDelete(id: number) {
    this.subjectIdToDelete = id;
  }

  deleteSubject() {
    this.subjectService.deleteSubject(this.subjectIdToDelete).subscribe(() => this.ngOnInit(), error => {
      this.createErrorModal(error.error.message);
    });
  }

  getTeacher(teacherId: number) {
    let user: User = new User();

    this.users.forEach(x => {
      if (x.id === teacherId){
        user = x;
        return false;
      }
    });

    return user.firstName + ' ' + user.lastName;
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
