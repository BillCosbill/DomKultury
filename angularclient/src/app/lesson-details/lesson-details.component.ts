import {Component, OnInit} from '@angular/core';
import {Lesson} from '../_model/lesson';
import {Student} from '../_model/student';
import {LessonService} from '../_services/lesson.service';
import {ActivatedRoute} from '@angular/router';
import {StudentService} from '../_services/student.service';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {Attendance} from '../_model/attendance';
import {AttendanceService} from '../_services/attendance.service';
import {StudentAttendance} from '../_model/student-attendance';

@Component({
  selector: 'app-lesson-details',
  templateUrl: './lesson-details.component.html',
  styleUrls: ['./lesson-details.component.css']
})
export class LessonDetailsComponent implements OnInit {

  lessonId: number;
  lesson: Lesson = new Lesson();

  subject: Subject = new Subject();

  students: Student[] = [];
  attendances: Attendance[] = [];

  studentsAttendance: StudentAttendance[] = [];

  constructor(private lessonService: LessonService, private subjectService: SubjectService, private route: ActivatedRoute, private studentService: StudentService, private attendanceService: AttendanceService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.lessonId = +params.id;
    });

    this.refreshData();
  }

  private refreshData() {
    this.students = [];
    this.attendances = [];
    this.studentsAttendance = [];

    this.lessonService.getLesson(this.lessonId).subscribe(result => {
      this.lesson = result;

      this.subjectService.getSubject(this.lesson.subjectId).subscribe(subject => {
        this.subject = subject;

        this.subject.studentsId.forEach(studentId => {
          this.studentService.getStudent(studentId).subscribe(student => {
            this.students.push(student);
            this.studentsAttendance.push(student as StudentAttendance);
          });
        });

        this.lesson.attendancesId.forEach(attendanceId => {
          this.attendanceService.getAttendance(attendanceId).subscribe(attendance => {
            this.attendances.push(attendance);

            this.studentsAttendance.forEach(studentAttendance => {
              if (studentAttendance.id === attendance.studentId) {
                studentAttendance.present = attendance.present;
              }
            });
          });
        });

      });
    });
  }

  save() {
    this.attendances.forEach(attendance => {
      this.studentsAttendance.forEach(studentAttendance => {
        if (attendance.studentId === studentAttendance.id) {
          attendance.present = studentAttendance.present;
        }
      });
    });

    this.lessonService.checkAttendance(this.attendances, this.lessonId).subscribe(() => {
      this.refreshData();
    });
  }

  changePresentValue(studentAttendance: StudentAttendance) {
    studentAttendance.present = !studentAttendance.present;
  }
}
