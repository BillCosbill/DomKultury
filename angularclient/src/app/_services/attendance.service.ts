import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Lesson} from '../_model/lesson';
import {catchError} from 'rxjs/operators';
import {Attendance} from '../_model/attendance';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private attendanceUrl: string;

  constructor(private http: HttpClient) {
    this.attendanceUrl = 'http://localhost:8081/attendances';
  }

  public findAll(): Observable<Attendance[]> {
    return this.http.get<Attendance[]>(this.attendanceUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getAttendance(id: number) {
    return this.http.get<Attendance>(this.attendanceUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteAttendance(id: number) {
    return this.http.delete<Attendance>(this.attendanceUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public updateAttendance(id: number, attendance: Attendance) {
    return this.http.put<Attendance>(this.attendanceUrl + '/' + id, attendance).pipe(
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
