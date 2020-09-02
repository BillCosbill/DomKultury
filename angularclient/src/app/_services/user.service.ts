import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from '../_model/user';
import {Event} from '../_model/event';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string;
  private changeRoleUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8081/users';
    this.changeRoleUrl = 'http://localhost:8081/changeRole';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  public getUser(id: number) {
    return this.http.get<User>(this.usersUrl + '/' + id);
  }

  public deleteUser(id: number) {
    return this.http.delete<User>(this.usersUrl + '/' + id);
  }

  public updateUser(user: User, id: number) {
    return this.http.put<User>(this.usersUrl + '/' + id, user);
  }

  public changeRole(role: string, id: number) {
    return this.http.patch<User>(this.changeRoleUrl + '/' + id + '?newRole=' + role, null);
  }
}
