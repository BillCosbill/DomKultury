import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Attendance} from '../_model/attendance';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private attendanceUrl: string;

  constructor(private http: HttpClient) {
    this.attendanceUrl = environment.apiAddress + '/attendances';
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

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }
}
