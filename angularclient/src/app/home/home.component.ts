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


  // TODO dodać do obiektu wydarzenie pole z krótkim opisem
  // TODO pobierając z bazy ustawić limit na 20 wydarzeń (musi być podzielne na 4 żeby na stronie się zmieściło ładnie
  // TODO przypadek kiedy jest w bazie mniej eventów niż 4 !?!?!?!?!
  config = {
    id: 'custom',
    itemsPerPage: 4,
    currentPage: 1,
    totalItems: 0
  };

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

    // TODO Formatowanie daty pobieranej z Eventów
    this.subjectService.findAll().subscribe(data => {
      this.subjects = data;
    });

    this.config.totalItems = this.subjects.length;
    this.eventObject.dataSource = this.scheduleData;
  }

  dataImportedIntoScheduler() {
    // @ts-ignore
    return this.eventObject.dataSource.length > 0;
  }
}
