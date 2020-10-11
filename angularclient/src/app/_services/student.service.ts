import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Subject} from '../_model/subject';
import {catchError} from 'rxjs/operators';
import {Student} from '../_model/student';
import {Lesson} from '../_model/lesson';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private studentUrl: string;

  constructor(private http: HttpClient) {
    this.studentUrl = 'http://localhost:8081/students';
  }

  public findAll(): Observable<Student[]> {
    return this.http.get<Student[]>(this.studentUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getStudent(id: number) {
    return this.http.get<Student>(this.studentUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public addStudent(student: Student) {
    return this.http.post<Student>(this.studentUrl, student).pipe(
      catchError(this.handleError)
    );
  }

  public deleteStudent(id: number) {
    return this.http.delete<Student>(this.studentUrl + '/' + id).pipe(
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
