import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit {

  subjects: Subject[] = [];

  constructor(private subjectService: SubjectService) {
  }

  ngOnInit() {
    this.subjectService.findAll().subscribe(data => {
      this.subjects = data;
    });
  }

  delete(id: number) {
    this.subjectService.deleteSubject(id).subscribe(() => this.ngOnInit());
  }
}
