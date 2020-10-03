import {Component, OnInit} from '@angular/core';
import {Lesson} from '../_model/lesson';
import {LessonService} from '../_services/lesson.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-lesson-details',
  templateUrl: './lesson-details.component.html',
  styleUrls: ['./lesson-details.component.css']
})
export class LessonDetailsComponent implements OnInit {

  lessonId: number;
  lesson: Lesson = new Lesson();



  constructor(private lessonService: LessonService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.lessonId = +params.id;
    });

    this.refreshData();
  }

  private refreshData() {
    this.lessonService.getLesson(this.lessonId).subscribe(result => {
      this.lesson = result;
    });
  }
}
