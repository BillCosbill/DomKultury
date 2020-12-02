import {Component, OnInit} from '@angular/core';
import {Student} from '../_model/student';
import {StudentService} from '../_services/student.service';
import {AuthService} from '../_services/auth.service';
import {ValidationService} from '../_services/validation/validation.service';

declare var openErrorModal;

@Component({
  selector: 'app-diary-students',
  templateUrl: './diary-students.component.html',
  styleUrls: ['./diary-students.component.css']
})
export class DiaryStudentsComponent implements OnInit {
  searchText;

  errorMessage: string = null;

  students: Student [] = [];
  studentIdSelected: number = null;

  studentModel: Student = new Student();

  config = {
    id: 'custom',
    itemsPerPage: 20,
    currentPage: 1,
    totalItems: this.students.length
  };

  constructor(private studentService: StudentService, public authService: AuthService, public validationService: ValidationService) {
  }

  ngOnInit() {
    this.studentService.findAll().subscribe(students => {
      this.students = students;
      this.students.sort((a, b) => (a.lastName.localeCompare(b.lastName)));
    });
  }

  deleteStudent(id: number) {
    this.studentService.deleteStudent(id).subscribe(() => {
      this.ngOnInit();
      this.validationService.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
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
      this.validationService.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
