import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {User} from '../_model/user';
import {Subject} from '../_model/subject';
import {catchError} from 'rxjs/operators';
import {environment} from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string;
  private activeAccountUrl: string;
  private changeRoleUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = environment.apiAddress + '/users';
    this.activeAccountUrl = environment.apiAddress + '/activeAccount';
    this.changeRoleUrl = environment.apiAddress + '/changeRole';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getUserSubjects(userId: number): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.usersUrl + '/' + userId + '/subjects').pipe(
      catchError(this.handleError)
    );
  }

  public getUser(id: number) {
    return this.http.get<User>(this.usersUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteUser(id: number) {
    return this.http.delete<User>(this.usersUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public updateUser(user: User, id: number) {
    return this.http.put<User>(this.usersUrl + '/' + id, user).pipe(
      catchError(this.handleError)
    );
  }

  public changeRole(role: string, id: number) {
    return this.http.patch<User>(this.usersUrl + '/changeRole/' + id + '?newRole=' + role, null).pipe(
      catchError(this.handleError)
    );
  }

  public changePassword(passwordRequest, id: number) {
    return this.http.patch(this.usersUrl + '/changePassword/' + id, {
      password: passwordRequest.password,
      newPassword: passwordRequest.newPassword
    }, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public generateNewPassword(userId: number) {
    return this.http.patch(this.usersUrl + '/generateNewPassword/' + userId, null, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }
}
