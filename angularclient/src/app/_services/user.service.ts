import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from '../_model/user';
import {Subject} from '../_model/subject';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string;
  private activeAccountUrl: string;
  private changeRoleUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8081/users';
    this.activeAccountUrl = 'http://localhost:8081/activeAccount';
    this.changeRoleUrl = 'http://localhost:8081/changeRole';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  public getUserSubjects(userId: number): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.usersUrl + '/' + userId + '/subjects');
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

  public activeAccount(id: number) {
    return this.http.patch<User>(this.usersUrl + '/activeAccount/' + id, null);
  }

  public changeRole(role: string, id: number) {
    return this.http.patch<User>(this.usersUrl + '/changeRole/' + id + '?newRole=' + role, null);
  }
}
