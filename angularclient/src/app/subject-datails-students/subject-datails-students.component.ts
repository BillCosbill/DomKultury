import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {StudentService} from '../_services/student.service';
import {Student} from '../_model/student';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';

@Component({
  selector: 'app-subject-datails-students',
  templateUrl: './subject-datails-students.component.html',
  styleUrls: ['./subject-datails-students.component.css']
})
export class SubjectDatailsStudentsComponent implements OnInit {

  subjectId: number;
  subject: Subject = new Subject();
  students: Student[] = [];

  constructor(private route: ActivatedRoute, private studentService: StudentService, private subjectService: SubjectService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.subjectId = +params['id'];
    });

    this.refreshData();
  }

  private refreshData() {
    this.subjectService.getSubject(this.subjectId).subscribe(result => {
      this.subject = result;

      this.students = [];

      this.subject.studentsId.forEach(x => {
        this.studentService.getStudent(x).subscribe(student => {
          this.students.push(student);
        });
      });
    });
  }

}
