import {Injectable} from '@angular/core';
import {StudentService} from '../student.service';
import {Student} from '../../_model/student';
import {User} from '../../_model/user';
import {UserService} from '../user.service';
import {AuthService} from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  students: Student[] = [];
  users: User[] = [];

  constructor(private studentService: StudentService, private  userService: UserService, private authService: AuthService) {
    this.refreshData();
  }

  refreshData() {
    if (this.authService.isTeacherLoggedIn()) {
      this.studentService.findAll().subscribe(students => {
        this.students = students;
      });

      this.userService.findAll().subscribe(users => {
        this.users = users;
      });
    }
  }

  studentExistsWithGivenEmail(email: string) {
    const studentWithSameEmail = this.students.find(student => student.email === email);
    const usertWithSameEmail = this.users.find(user => user.email === email);
    return (studentWithSameEmail !== null && studentWithSameEmail !== undefined) || (usertWithSameEmail !== null && usertWithSameEmail !== undefined);
  }

  validateBirthdayIfCorrect(dateToValidate: string) {
    const date = Date.parse(dateToValidate);
    const dateToday = Date.now();
    return !isNaN(date) && (date < dateToday);
  }

  lessonTookPlace(lessonDate: string) {
    if (lessonDate !== undefined) {
      const date = Date.parse(lessonDate);
      const dateToday = Date.now();

      return date < dateToday;
    }
  }
}
