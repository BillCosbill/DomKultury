import {Component, OnInit} from '@angular/core';
import {Student} from '../_model/student';
import {StudentService} from '../_services/student.service';
import {AuthService} from '../_services/auth.service';
import {ValidationService} from '../_services/validation/validation.service';

@Component({
  selector: 'app-diary-students',
  templateUrl: './diary-students.component.html',
  styleUrls: ['./diary-students.component.css']
})
export class DiaryStudentsComponent implements OnInit {

  students: Student [] = [];
  studentIdSelected: number = null;

  studentModel: Student = new Student();

  constructor(private studentService: StudentService, private authService: AuthService, private validationService: ValidationService) {
  }

  ngOnInit() {
    this.studentService.findAll().subscribe(students => {
      this.students = students;
    });
  }

  deleteStudent(id: number) {
    this.studentService.deleteStudent(id).subscribe(() => this.ngOnInit());
  }

  selectStudentId(id: number) {
    this.studentIdSelected = id;
  }

  startAddingStudent() {
    this.studentModel = new Student();
  }

  addStudent() {
    this.studentService.addStudent(this.studentModel).subscribe(() => {
      this.ngOnInit();
    });
  }
}
