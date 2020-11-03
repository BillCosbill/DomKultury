import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Subject} from '../_model/subject';
import {Student} from '../_model/student';

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

  public addSubject(subject: Subject) {
    return this.http.post<Subject>(this.subjectUrl, subject).pipe(
      catchError(this.handleError)
    );
  }

  public deleteSubject(id: number) {
    return this.http.delete<Subject>(this.subjectUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public updateSubject(subject: Subject, id: number) {
    return this.http.put<Subject>(this.subjectUrl + '/' + id, subject).pipe(
      catchError(this.handleError)
    );
  }

  //TODO zrobić jakieś popapy z errorami i z udanymi akcjami np addUserToEvent
  //TODO dodać obsługę błędów w innych serwisach !!!!!!!!!!!!!!!!
  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

  deleteStudentFromSubject(subjectId: number, studentIdToDelete: number) {
    return this.http.delete(this.subjectUrl + '/' + subjectId + '/deleteStudent/' + studentIdToDelete).pipe(
      catchError(this.handleError)
    );
  }

  addStudentToSubject(studentToAdd: Student, subjectId: number) {
    return this.http.post<Student>(this.subjectUrl + '/' + subjectId + '/addStudent', studentToAdd).pipe(
      catchError(this.handleError)
    );
  }

  addStudentFromDatabaseToSubject(subjectId: number, studentId: number) {
    return this.http.patch(this.subjectUrl + '/' + subjectId + '/addStudentFromDatabase/' + studentId, null).pipe(
      catchError(this.handleError)
    );
  }
}
