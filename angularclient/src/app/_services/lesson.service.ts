import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Lesson} from '../_model/lesson';

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

  //TODO zrobić jakieś popapy z errorami i z udanymi akcjami np addUserToEvent
  //TODO dodać obsługę błędów w innych serwisach !!!!!!!!!!!!!!!!
  handleError(error: HttpErrorResponse) {
    console.log(error.error.message);
    return throwError(error);
  }
}
