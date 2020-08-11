import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from '../_model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string;
  private userGiveAdminUrl: string;
  private userGiveTeacherUrl: string;
  private userGiveUserUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8081/users';
    this.userGiveAdminUrl = 'http://localhost:8081/userGiveAdmin';
    this.userGiveTeacherUrl = 'http://localhost:8081/userGiveTeacher';
    this.userGiveUserUrl = 'http://localhost:8081/userGiveUser';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  public deleteUser(id: number) {
    return this.http.delete<User>(this.usersUrl + '/' + id);
  }

  public updateUser(user: User) {
    return this.http.put<User>(this.usersUrl, user);
  }

  // TODO przenieść trzy metody zmiany roli do jednej
  public giveAdmin(user: User) {
    return this.http.patch<User>(this.userGiveAdminUrl, user);
  }

  public giveTeacher(user: User) {
    return this.http.patch<User>(this.userGiveTeacherUrl, user);
  }

  public giveUser(user: User) {
    return this.http.patch<User>(this.userGiveUserUrl, user);
  }

}
