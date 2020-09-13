import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Room} from '../_model/room';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  private roomUrl: string;

  constructor(private http: HttpClient) {
    this.roomUrl = 'http://localhost:8081/rooms';
  }

  public findAll(): Observable<Room[]> {
    return this.http.get<Room[]>(this.roomUrl).pipe(
      catchError(this.handleError)
    );
  }

  public addRoom(room: Room) {
    return this.http.post<Room>(this.roomUrl, room);
  }

  public getRoom(id: number) {
    return this.http.get<Room>(this.roomUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteRoom(id: number) {
    return this.http.delete<Room>(this.roomUrl + '/' + id).pipe(
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
