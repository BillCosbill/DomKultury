import {Injectable, OnInit} from '@angular/core';
import {StudentService} from '../student.service';
import {Student} from '../../_model/student';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  students: Student[] = [];

  constructor(private studentService: StudentService) {
    this.studentService.findAll().subscribe(students => {
      this.students = students;
    });
  }

  studentExistsWithGivenPesel(pesel: string) {
    const studentWithSamePesel = this.students.find(student => student.pesel === pesel);
    return studentWithSamePesel !== null && studentWithSamePesel !== undefined;
  }

  studentExistsWithGivenEmail(email: string) {
    const studentWithSameEmail = this.students.find(student => student.email === email);
    return studentWithSameEmail !== null && studentWithSameEmail !== undefined;
  }

  validateBirthdayIfCorrect(dateToValidate: string) {
    const date = Date.parse(dateToValidate);
    const dateToday = Date.now();
    return !isNaN(date) && (date < dateToday);
  }
}
