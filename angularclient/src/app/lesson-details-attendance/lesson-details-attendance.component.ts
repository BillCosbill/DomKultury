import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {LessonService} from '../_services/lesson.service';
import {StudentAttendance} from '../_model/student-attendance';
import {Lesson} from '../_model/lesson';
import {Subject} from '../_model/subject';
import {Attendance} from '../_model/attendance';
import {SubjectService} from '../_services/subject.service';
import {StudentService} from '../_services/student.service';
import {AttendanceService} from '../_services/attendance.service';
import {formatDate} from '@angular/common';
import {AuthService} from '../_services/auth.service';
import {TokenStorageService} from '../_services/token-storage.service';

declare var openErrorModal;

@Component({
  selector: 'app-lesson-details-attendance',
  templateUrl: './lesson-details-attendance.component.html',
  styleUrls: ['./lesson-details-attendance.component.css']
})
export class LessonDetailsAttendanceComponent implements OnInit {
  errorMessage: string = null;

  lessonId: number;
  subjectId: number;

  lesson: Lesson = new Lesson();

  subject: Subject = new Subject();

  attendances: Attendance[] = [];

  studentsAttendance: StudentAttendance[] = [];

  constructor(public authService: AuthService, private token: TokenStorageService, private lessonService: LessonService, private subjectService: SubjectService, private route: ActivatedRoute, private studentService: StudentService, private attendanceService: AttendanceService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.subjectId = +params.id;
      this.lessonId = +params.id2;
    });

    this.attendances = [];
    this.studentsAttendance = [];

    this.lessonService.getStudentAttendances(this.lessonId).subscribe(studentAttendance => {
      this.studentsAttendance = studentAttendance;
      this.studentsAttendance.sort((a, b) => (a.lastName.localeCompare(b.lastName)));
    });

    this.lessonService.getLesson(this.lessonId).subscribe(result => {
      this.lesson = result;

      this.subjectService.getSubject(this.lesson.subjectId).subscribe(subject => {
        this.subject = subject;

        this.lesson.attendancesId.forEach(attendanceId => {
          this.attendanceService.getAttendance(attendanceId).subscribe(attendance => {
            this.attendances.push(attendance);
          });
        });

      });
    });
  }


  save() {
    this.attendances.forEach(attendance => {
      attendance.present = this.studentsAttendance.find(x => x.id === attendance.studentId).present;
    });

    this.lessonService.checkAttendance(this.attendances, this.lessonId).subscribe(() => {
      this.ngOnInit();
    }, error => {
      this.createErrorModal(error.error.message);
    });
  }

  changePresentValue(studentAttendance: StudentAttendance) {
    studentAttendance.present = !studentAttendance.present;
  }

  userIsOwner() {
    return this.token.getUser().id === this.subject.teacherId || this.authService.isAdminLoggedIn();
  }

  enableToCheckAttendance() {
    const todayDate = formatDate(new Date(), 'yyyy-MM-dd', 'en');
    const currentTime = (new Date()).toLocaleString('en-US', {hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false});

    let lessonDate = null;
    let lessonStartTime = null;
    let lessonEndTime = null;

    if (this.lesson.startDate !== undefined) {
      lessonDate = this.lesson.startDate.substr(0, 10);
      lessonStartTime = this.lesson.startDate.substr(11, 8);
      lessonEndTime = this.lesson.finishDate.substr(11, 8);
    }

    return todayDate === lessonDate && currentTime >= lessonStartTime && currentTime <= lessonEndTime;
  }

  createErrorModal(message: string) {
    this.errorMessage = message;
    openErrorModal();
  }
}






