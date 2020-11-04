import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';
import {AuthService} from '../_services/auth.service';

declare var openErrorModal;

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit {
  errorMessage: string = null;

  subjects: Subject[] = [];
  users: User[] = [];

  subjectIdToDelete: number = null;
  subjectToAdd: Subject = new Subject();

  constructor(private subjectService: SubjectService, private userService: UserService, private authService: AuthService) {
  }

  ngOnInit() {
    this.subjectService.findAll().subscribe(subject => {
      this.subjects = subject;
      this.subjects.sort((a, b) => (a.name.localeCompare(b.name)));
    });

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });
  }

  delete(id: number) {
    this.subjectService.deleteSubject(id).subscribe(() => this.ngOnInit(), error => {
      this.createErrorModal(error.error.message);
    });
  }

  getTeacher(teacherId: number) {
    let user: User = new User();

    this.users.forEach(x => {
      if (x.id === teacherId){
        user = x;
      }
    });

    return user.firstName + ' ' + user.lastName;
  }

  deleteSubject(id: number) {
    this.subjectService.deleteSubject(id).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  selectSubjectIdToDelete(id: number) {
    this.subjectIdToDelete = id;
  }

  startAddingSubject() {
    this.subjectToAdd = new Subject();
  }

  addSubject() {
    this.subjectService.addSubject(this.subjectToAdd).subscribe(() => {
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
