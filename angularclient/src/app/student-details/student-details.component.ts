import {Component, OnInit} from '@angular/core';
import {Student} from '../_model/student';
import {StudentService} from '../_services/student.service';
import {ActivatedRoute} from '@angular/router';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {User} from '../_model/user';
import {UserService} from '../_services/user.service';
import {AuthService} from '../_services/auth.service';

declare var openErrorModal;

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrls: ['./student-details.component.css']
})
export class StudentDetailsComponent implements OnInit {
  errorMessage: string = null;

  student: Student = new Student();
  studentId: number;

  subjects: Subject[] = [];
  users: User[] = [];

  studentEdited: Student = new Student();

  constructor(private authService: AuthService, private studentService: StudentService, private route: ActivatedRoute, private subjectService: SubjectService, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.studentId = +params['id'];
    });

    this.studentService.getStudent(this.studentId).subscribe(student => {
      this.student = student;
    });

    this.subjects = [];

    this.subjectService.findAll().subscribe(subjects => {
      subjects.forEach(subject => {
        if (subject.studentsId.includes(this.studentId)) {
          this.subjects.push(subject);
        }
      });
    });

    this.userService.findAll().subscribe(users => {
      this.users = users;
    });
  }

  getTeacher(teacherId: number) {
    let user: User = new User();

    this.users.forEach(x => {
      if (x.id === teacherId){
        user = x;
      }
    });

    return user.firstName + ' ' + user.lastName;
  }

  editStudent() {
    this.studentService.updateStudent(this.studentEdited, this.studentId).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  startEditing() {
    this.studentEdited = new Student();
    this.studentEdited.id = this.student.id;
    this.studentEdited.firstName = this.student.firstName;
    this.studentEdited.lastName = this.student.lastName;
    this.studentEdited.pesel = this.student.pesel;
    this.studentEdited.email = this.student.email;
    this.studentEdited.birthday = this.student.birthday;
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }


}
