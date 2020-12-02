import {Component, OnInit} from '@angular/core';
import {EventSettingsModel, View} from '@syncfusion/ej2-angular-schedule';
import {SubjectService} from '../_services/subject.service';
import {Subject} from '../_model/subject';
import {DataSource} from '../_model/data-source';
import {LessonService} from '../_services/lesson.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public setView: View = 'Month';
  public weekFirstDay = 1;
  public eventObject: EventSettingsModel = {
    dataSource: [{}]
  };

  scheduleData: DataSource[] = [];

  subjects: Subject[] = [];

  constructor(private subjectService: SubjectService, private lessonService: LessonService) {
  }

  ngOnInit() {
    this.scheduleData = [];

    this.lessonService.findAll().subscribe(lessons => {
      lessons.forEach(lesson => {
        this.scheduleData.push(new DataSource(lesson.topic, lesson.startDate, lesson.finishDate));
      });
    });

    this.eventObject.dataSource = this.scheduleData;

    this.subjectService.findAll().subscribe(data => {
      this.subjects = data;
    });

    this.eventObject.dataSource = this.scheduleData;
  }

  dataImportedIntoScheduler() {
    // @ts-ignore
    return this.eventObject.dataSource.length > 0;
  }
}
