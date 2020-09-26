import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Subject} from '../_model/subject';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private subjectUrl: string;

  constructor(private http: HttpClient) {
    this.subjectUrl = 'http://localhost:8081/subjects';
  }

  public findAll(): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.subjectUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getSubject(id: number) {
    return this.http.get<Subject>(this.subjectUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteSubject(id: number) {
    return this.http.delete<Subject>(this.subjectUrl + '/' + id).pipe(
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
