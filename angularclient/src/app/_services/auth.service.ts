import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {TokenStorageService} from './token-storage.service';
import {catchError} from 'rxjs/operators';
import {environment} from '../../environments/environment';

const AUTH_API = environment.apiAddress + '/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
  }

  public login(credentials): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      username: credentials.username,
      password: credentials.password
    }, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public register(user): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: user.password
    }, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public isAdminLoggedIn() {
    let roles: string[] = [];
    if (this.tokenStorage.getUser() !== null) {
      roles = this.tokenStorage.getUser().roles;
    }
    return roles.includes('ROLE_ADMIN');
  }

  public isTeacherLoggedIn() {
    let roles: string[] = [];
    if (this.tokenStorage.getUser() !== null) {
      roles = this.tokenStorage.getUser().roles;
    }
    return roles.includes('ROLE_TEACHER') || roles.includes('ROLE_ADMIN');
  }

  public isUserLoggedIn() {
    let roles: string[] = [];
    if (this.tokenStorage.getUser() !== null) {
      roles = this.tokenStorage.getUser().roles;
    }
    return roles.includes('ROLE_USER')  || roles.includes('ROLE_TEACHER') || roles.includes('ROLE_ADMIN');
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }
}
