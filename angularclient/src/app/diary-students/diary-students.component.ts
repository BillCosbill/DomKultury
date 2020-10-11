import {Component, OnInit} from '@angular/core';
import {Student} from '../_model/student';
import {StudentService} from '../_services/student.service';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-diary-students',
  templateUrl: './diary-students.component.html',
  styleUrls: ['./diary-students.component.css']
})
export class DiaryStudentsComponent implements OnInit {

  students: Student [] = [];
  studentIdToDelete: number = null;

  studentToAdd: Student = new Student();

  constructor(private studentService: StudentService, private authService: AuthService) {
  }

  ngOnInit() {
    this.studentService.findAll().subscribe(students => {
      this.students = students;
    });
  }

  deleteStudent(id: number) {
    this.studentService.deleteStudent(id).subscribe(() => this.ngOnInit());
  }

  selectStudentIdToDelete(id: number) {
    this.studentIdToDelete = id;
  }

  startAddingStudent() {
    this.studentToAdd = new Student();
  }

  validateDate(dateToValidate: string) {
    const date = Date.parse(dateToValidate);
    return isNaN(date);
  }

  addStudent() {
    this.studentService.addStudent(this.studentToAdd).subscribe(() => {
      this.ngOnInit();
    });
  }
}
