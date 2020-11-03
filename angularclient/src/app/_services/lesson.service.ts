import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Lesson} from '../_model/lesson';
import {Attendance} from '../_model/attendance';

@Injectable({
  providedIn: 'root'
})
export class LessonService {

  private lessonUrl: string;

  constructor(private http: HttpClient) {
    this.lessonUrl = 'http://localhost:8081/lessons';
  }

  public findAll(): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(this.lessonUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getLesson(id: number) {
    return this.http.get<Lesson>(this.lessonUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteLesson(id: number) {
    return this.http.delete<Lesson>(this.lessonUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public addLesson(lesson: Lesson) {
    return this.http.post<Lesson>(this.lessonUrl, lesson).pipe(
      catchError(this.handleError)
    );
  }

  public updateLesson(lesson: Lesson, id: number) {
    return this.http.put<Lesson>(this.lessonUrl + '/' + id, lesson).pipe(
      catchError(this.handleError)
    );
  }

  public checkAttendance(attendances: Attendance[], id: number) {
    return this.http.patch<Attendance[]>(this.lessonUrl + '/' + id + '/checkAttendance', attendances).pipe(
      catchError(this.handleError)
    );
  }

  //TODO zrobić jakieś popapy z errorami i z udanymi akcjami np addUserToEvent
  //TODO dodać obsługę błędów w innych serwisach !!!!!!!!!!!!!!!!
  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

}
