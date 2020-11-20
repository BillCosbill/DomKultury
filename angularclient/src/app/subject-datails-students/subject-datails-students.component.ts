import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {StudentService} from '../_services/student.service';
import {Student} from '../_model/student';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {AuthService} from '../_services/auth.service';
import {ValidationService} from '../_services/validation/validation.service';
import {EmailMessage} from '../_model/email-message';
import {TokenStorageService} from '../_services/token-storage.service';
import {EmailService} from '../_services/email.service';

declare var openErrorModal;

@Component({
  selector: 'app-subject-datails-students',
  templateUrl: './subject-datails-students.component.html',
  styleUrls: ['./subject-datails-students.component.css']
})
export class SubjectDatailsStudentsComponent implements OnInit {
  errorMessage: string = null;

  subjectId: number;
  subject: Subject = new Subject();
  students: Student[] = [];

  allStudents: Student[] = [];

  studentIdToDelete: number = null;
  studentToAdd: Student = new Student();

  emailMessage: EmailMessage = new EmailMessage();

  constructor(private token: TokenStorageService, private route: ActivatedRoute, private studentService: StudentService, private subjectService: SubjectService,
              public authService: AuthService, public validationService: ValidationService, private emailService: EmailService) { }

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
          this.students.sort((a, b) => (a.lastName.localeCompare(b.lastName)));
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
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  startAddingStudent() {
    this.studentToAdd = new Student();
  }

  addStudent() {
    this.subjectService.addStudentToSubject(this.studentToAdd, this.subjectId).subscribe(() => {
      this.refreshData();
      this.validationService.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  addStudentToSubject() {
    this.subjectService.addStudentFromDatabaseToSubject(this.subjectId, this.studentToAdd.id).subscribe(() => {
      this.refreshData();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  openEmailModal() {
    this.emailMessage = new EmailMessage();
  }

  sendMultipleEmails() {
    this.emailMessage.studentList = this.students;
    this.emailMessage.fromId = this.token.getUser().id;

    this.emailService.sendMultipleMails(this.emailMessage).subscribe(() => this.ngOnInit(),
      error => {
        this.createErrorModal(error.error.message);
      });
  }

  userIsOwner() {
    return this.token.getUser().id === this.subject.teacherId || this.authService.isAdminLoggedIn();
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}
