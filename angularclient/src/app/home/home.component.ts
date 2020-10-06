import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {
  View,
  EventSettingsModel,
  DayService,
  WeekService,
  WorkWeekService,
  MonthService,
  AgendaService, ResizeService, DragAndDropService
} from '@syncfusion/ej2-angular-schedule';

import { L10n, loadCldr } from '@syncfusion/ej2-base';
import {SubjectService} from '../_services/subject.service';
import {Subject} from '../_model/subject';

declare let require: Function;
loadCldr(
  require('cldr-data/supplemental/numberingSystems.json'),
  require('cldr-data/main/pl/ca-gregorian.json'),
  require('cldr-data/main/pl/numbers.json'),
  require('cldr-data/main/pl/timeZoneNames.json'),
  require('cldr-data/main/pl/currencies.json'),
);
L10n.load({
  pl: {
    schedule: {
      day: 'Dzień',
      week: 'Tydzień',
      workWeek: 'Tydzień roboczy',
      month: 'Miesiąc',
      agenda: 'Agenda',
      weekAgenda: 'Agenda tygodniowa',
      workWeekAgenda: 'Agenda tygodnia roboczego',
      monthAgenda: 'Miesięczna agenda',
      today: 'Dzisiaj',
      noEvents: 'Brak wydarzeń',
      emptyContainer: 'Tego dnia nie ma żadnych wydarzeń.',
      allDay: 'Wszystkie dni',
      start: 'Start',
      end: 'Koniec',
      more: 'Więcej',
      close: 'Zamknij',
      cancel: 'Anuluj',
      noTitle: '(Brak tytułu)',
      delete: 'Usuń',
      deleteEvent: 'Usuń wydarzenie',
      deleteMultipleEvent: 'Usuń wiele wydarzeń',
      selectedItems: 'Wybrany przedmiot',
      deleteSeries: 'Usuń serię',
      edit: 'Edytuj',
      editSeries: 'Edytuj serię',
      editEvent: 'Edytuj wydarzenie',
      createEvent: 'Stwórz wydarzenie',
      subject: 'Temat',
      addTitle: 'Dodaj tytuł',
      moreDetails: 'Więcej szczegółów',
      save: 'Zapisz',
      editContent: 'Chcesz edytować tylko to wydarzenie czy całą serię?',
      deleteRecurrenceContent: 'Chcesz usunąć tylko to wydarzenie czy całą serię?',
      deleteContent: 'Czy na pewno chcesz usunąć to wydarzenie?',
      deleteMultipleContent: 'Czy na pewno chcesz usunąć wybrane wydarzenia?',
      newEvent: 'Nowe wydarzenie',
      title: 'Tytuł',
      location: 'Lokalizacja',
      description: 'Opis',
      timezone: 'Strefa czasowa',
      startTimezone: 'Startowa strefa czasowa',
      endTimezone: 'Końcowa strefa czasowa',
      repeat: 'Powtórz',
      saveButton: 'Zapisz',
      cancelButton: 'Anuluj',
      deleteButton: 'Usuń',
      recurrence: 'Powtarzaj',
      wrongPattern: 'Wzorzec powtarzania jest nieprawidłowy.',
      seriesChangeAlert: 'Zmiany wprowadzone w określonych instancjach tej serii zostaną anulowane, a wydarzenia te będą ponownie pasować do serii.',
      createError: 'Czas trwania wydarzenia musi być krótszy niż częstotliwość jego występowania. Skróć czas trwania lub zmień wzorzec cyklu w edytorze zdarzeń cyklu.',
      recurrenceDateValidation: 'Some months have fewer than the selected date. For these months, the occurrence will fall on the last date of the month.',
      sameDayAlert: 'Dwa wystąpienia tego samego zdarzenia nie mogą wystąpić tego samego dnia.',
      editRecurrence: 'Edytuj cykl',
      repeats: 'Powtórzenia',
      alert: 'Ostrzeżenie',
      startEndError: 'Wybrana data zakończenia przypada przed datą rozpoczęcia.',
      invalidDateError: 'Wprowadzona wartość daty jest nieprawidłowa.',
      ok: 'Ok',
      occurrence: 'Występowanie',
      series: 'Seria',
      previous: 'Pooprzedni',
      next: 'Następny',
      timelineDay: 'Dzień osi czasu',
      timelineWeek: 'Tydzień osi czasu',
      timelineWorkWeek: 'Tydzień roboczy osi czasu',
      timelineMonth: 'Miesiąc osi czasu'
    },
    recurrenceeditor: {
      none: 'Żaden',
      daily: 'Dzienny',
      weekly: 'Tygodniowy',
      monthly: 'Miesięczny',
      month: 'Miesiąc',
      yearly: 'Roczny',
      never: 'Nigdy',
      until: 'Do',
      count: 'Licz',
      first: 'Pierwszy',
      second: 'Drugi',
      third: 'Trzeci',
      fourth: 'Czwarty',
      last: 'Ostatni',
      repeat: 'Powtórz',
      repeatEvery: 'Powtórz każdy',
      on: 'Powtarzanie włączone',
      end: 'Koniec',
      onDay: 'Dzień',
      days: 'Dni',
      weeks: 'Tygodnie',
      months: 'Miesiące',
      years: 'Lata',
      every: 'Każdy',
      summaryTimes: 'razy',
      summaryOn: 'na',
      summaryUntil: 'do',
      summaryRepeat: 'powtórzenia',
      summaryDay: 'dni',
      summaryWeek: 'tygodnie',
      summaryMonth: 'miesiące',
      summaryYear: 'lata'
    },
    calendar: {
      today: 'Dzisiaj'
    },
  }
});


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [DayService, WeekService, WorkWeekService, MonthService, AgendaService, ResizeService, DragAndDropService],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  public setView: View = 'Month';
  public weekFirstDay = 1;

  public eventObject: EventSettingsModel = {
    dataSource: [{
      Subject: 'Tańce',
      StartTime: new Date(2020, 8, 7, 12, 0),
      EndTime: new Date(2020, 8, 7, 14, 0)
    }]
  };

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

  constructor(private subjectService: SubjectService) {
  }


  ngOnInit() {
    // TODO Formatowanie daty pobieranej z Eventów
    this.subjectService.findAll().subscribe(data => {
      this.subjects = data;
    });

    this.config.totalItems = this.subjects.length;
  }
}
