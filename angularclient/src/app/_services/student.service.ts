import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Student} from '../_model/student';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private studentUrl: string;

  constructor(private http: HttpClient) {
    this.studentUrl = environment.apiAddress + '/students';
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

  public updateStudent(student: Student, id: number) {
    return this.http.put<Student>(this.studentUrl + '/' + id, student).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }
}
