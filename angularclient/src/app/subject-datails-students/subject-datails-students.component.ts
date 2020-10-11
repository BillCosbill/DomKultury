import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {StudentService} from '../_services/student.service';
import {Student} from '../_model/student';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-subject-datails-students',
  templateUrl: './subject-datails-students.component.html',
  styleUrls: ['./subject-datails-students.component.css']
})
export class SubjectDatailsStudentsComponent implements OnInit {

  subjectId: number;
  subject: Subject = new Subject();
  students: Student[] = [];

  allStudents: Student[] = [];

  studentIdToDelete: number = null;
  studentToAdd: Student = new Student();

  constructor(private route: ActivatedRoute, private studentService: StudentService, private subjectService: SubjectService, private authService: AuthService) { }

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

    this.studentService.findAll().subscribe(students => {
      this.allStudents = students;
    });
  }

  selectStudentIdToDelete(id: number) {
    this.studentIdToDelete = id;
  }

  deleteStudentFromSubject() {
    this.subjectService.deleteStudentFromSubject(this.subjectId, this.studentIdToDelete).subscribe(() => {
      this.refreshData();
    });
  }

  startAddingStudent() {
    this.studentToAdd = new Student();
  }

  validateDate(dateToValidate: string) {
    const date = Date.parse(dateToValidate);
    return isNaN(date);
  }

  addStudent() {
    this.subjectService.addStudentToSubject(this.studentToAdd, this.subjectId).subscribe(() => {
      this.refreshData();
    });
  }

  addStudentToSubject() {
    this.subjectService.addStudentFromDatabaseToSubject(this.subjectId, this.studentToAdd.id).subscribe(() => {
      this.refreshData();
    });
  }
}
