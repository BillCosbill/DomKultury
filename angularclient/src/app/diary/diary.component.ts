import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {User} from '../_model/user';
import {TokenStorageService} from '../_services/token-storage.service';
import {AuthService} from '../_services/auth.service';

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

  subjectIdToDelete: number = null;

  constructor(private subjectService: SubjectService, private token: TokenStorageService, public authService: AuthService) {
  }

  ngOnInit() {
    this.currentUser = this.token.getUser();

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

  delete(id: number) {
    this.subjectService.deleteSubject(id).subscribe(() => this.ngOnInit(), error => {
      this.createErrorModal(error.error.message);
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

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
