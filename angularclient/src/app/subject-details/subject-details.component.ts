import {Component, OnInit} from '@angular/core';
import {Subject} from '../_model/subject';
import {SubjectService} from '../_services/subject.service';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../_services/user.service';
import {User} from '../_model/user';
import {Student} from '../_model/student';
import {StudentService} from '../_services/student.service';
import {Lesson} from '../_model/lesson';
import {LessonService} from '../_services/lesson.service';

@Component({
  selector: 'app-subject-details',
  templateUrl: './subject-details.component.html',
  styleUrls: ['./subject-details.component.css']
})
export class SubjectDetailsComponent implements OnInit {

  subjectId: number;
  subject: Subject = new Subject();

  teacher: User = new User();
  students: Student[] = [];
  lessons: Lesson[] = [];

  constructor(private subjectService: SubjectService, private route: ActivatedRoute, private userService: UserService, private studentService: StudentService, private lessonService: LessonService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.subjectId = +params['id'];
    });

    this.refreshData();
  }

  private refreshData() {
    this.subjectService.getSubject(this.subjectId).subscribe(result => {
      this.subject = result;

      this.userService.getUser(this.subject.teacherId).subscribe(user => {
        this.teacher = user;
      });

      this.students = [];

      this.subject.studentsId.forEach(x => {
        this.studentService.getStudent(x).subscribe(student => {
          this.students.push(student);
        });
      });

      this.lessons = [];
      this.subject.lessonsId.forEach(x => {
        this.lessonService.getLesson(x).subscribe(lesson => {
          this.lessons.push(lesson);
        });
      });
    });


  }
}
