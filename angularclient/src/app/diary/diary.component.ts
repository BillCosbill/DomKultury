import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {User} from '../_model/user';
import {TokenStorageService} from '../_services/token-storage.service';

@Component({
  selector: 'app-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.css']
})
export class DiaryComponent implements OnInit {

  subjects: Subject[] = [];
  currentUser: User;

  constructor(private subjectService: SubjectService, private token: TokenStorageService) {
  }

  ngOnInit() {
    this.currentUser = this.token.getUser();

    this.subjects = [];

    this.subjectService.findAll().subscribe(data => {
      data.forEach(subject => {
        if (subject.teacherId === this.currentUser.id) {
          this.subjects.push(subject);
        }
      });
    });
  }

  delete(id: number) {
    this.subjectService.deleteSubject(id).subscribe(() => this.ngOnInit());
  }
}